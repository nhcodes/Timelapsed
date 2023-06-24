package codes.nh.timelapsed.export

import java.io.ByteArrayOutputStream
import java.io.File

/*
https://learn.microsoft.com/en-us/windows/win32/directshow/avi-riff-file-reference
https://xoax.net/sub_web/ref_dev/fileformat_avi/
https://cdn.hackaday.io/files/274271173436768/avi.pdf
https://github.com/monceaux/java-mjpeg/blob/master/src/wpm/mjpeg/MJPEGGenerator.java
*/
class ImagesToVideo(
    val file: File,
    val width: Int,
    val height: Int,
    val frameRate: Int,
    val frameCount: Int
) {

    private val outputStream = file.outputStream()
    private val channel = outputStream.channel

    private var movieListOffset = 0L

    init {
        outputStream.apply {
            write(RiffHeader().toBytes())
            write(HeaderList().toBytes())
            write(AviMainHeader().toBytes())
            write(StreamList().toBytes())
            write(AviStreamHeader().toBytes())
            write(AviStreamFormat().toBytes())
            movieListOffset = channel.position()
            write(MovieList().toBytes())
        }
    }

    private inner class RiffHeader {
        val fcc = "RIFF"
        val size = 0 //gets overwritten later
        val type = "AVI "
        fun toBytes() = concatData(
            fcc,
            size,
            type
        )
    }

    private inner class HeaderList {
        val fcc = "LIST"
        val size = 192
        val type = "hdrl"
        fun toBytes() = concatData(
            fcc,
            size,
            type
        )
    }

    //https://learn.microsoft.com/en-us/windows/win32/api/aviriff/ns-aviriff-avimainheader
    private inner class AviMainHeader {
        val fcc = "avih"
        val cb = 56
        val dwMicroSecPerFrame = 1000000 / frameRate
        val dwMaxBytesPerSec = 0
        val dwPaddingGranularity = 0
        val dwFlags = 0
        val dwTotalFrames = frameCount
        val dwInitialFrames = 0
        val dwStreams = 1
        val dwSuggestedBufferSize = 0
        val dwWidth = width
        val dwHeight = height
        val dwReserved = IntArray(4)
        fun toBytes() = concatData(
            fcc,
            cb,
            dwMicroSecPerFrame,
            dwMaxBytesPerSec,
            dwPaddingGranularity,
            dwFlags,
            dwTotalFrames,
            dwInitialFrames,
            dwStreams,
            dwSuggestedBufferSize,
            dwWidth,
            dwHeight,
            dwReserved[0],
            dwReserved[1],
            dwReserved[2],
            dwReserved[3],
        )
    }

    private inner class StreamList {
        val fcc = "LIST"
        val size = 116
        val type = "strl"
        fun toBytes() = concatData(
            fcc,
            size,
            type
        )
    }

    //https://learn.microsoft.com/en-us/windows/win32/api/aviriff/ns-aviriff-avistreamheader
    private inner class AviStreamHeader {
        val fcc = "strh"
        val cb = 56
        val fccType = "vids"
        val fccHandler = "mjpg" //?
        val dwFlags = 0
        val wPriority: Short = 0
        val wLanguage: Short = 0
        val dwInitialFrames = 0
        val dwScale = 1
        val dwRate = frameRate
        val dwStart = 0
        val dwLength = frameCount
        val dwSuggestedBufferSize = 0
        val dwQuality = -1
        val dwSampleSize = 0
        val rcFrameLeft: Short = 0
        val rcFrameTop: Short = 0
        val rcFrameRight: Short = 0
        val rcFrameBottom: Short = 0
        fun toBytes() = concatData(
            fcc,
            cb,
            fccType,
            fccHandler,
            dwFlags,
            wPriority,
            wLanguage,
            dwInitialFrames,
            dwScale,
            dwRate,
            dwStart,
            dwLength,
            dwSuggestedBufferSize,
            dwQuality,
            dwSampleSize,
            rcFrameLeft,
            rcFrameTop,
            rcFrameRight,
            rcFrameBottom,
        )
    }

    //https://learn.microsoft.com/en-us/windows/win32/api/wingdi/ns-wingdi-bitmapinfoheader
    private inner class AviStreamFormat {
        val fcc = "strf"
        val cb = 40
        val biSize = 40
        val biWidth = width
        val biHeight = height
        val biPlanes: Short = 1
        val biBitCount: Short = 16 //?
        val biCompression = "mjpg" //todo probably wrong
        val biSizeImage = 0
        val biXPelsPerMeter = 0
        val biYPelsPerMeter = 0
        val biClrUsed = 0
        val biClrImportant = 0
        fun toBytes() = concatData(
            fcc,
            cb,
            biSize,
            biWidth,
            biHeight,
            biPlanes,
            biBitCount,
            biCompression,
            biSizeImage,
            biXPelsPerMeter,
            biYPelsPerMeter,
            biClrUsed,
            biClrImportant,
        )
    }

    private inner class MovieList {
        val fcc = "LIST"
        val size = 0 //gets overwritten later
        val type = "movi"
        fun toBytes() = concatData(
            fcc,
            size,
            type
        )
    }

    fun addImage(imageBytes: ByteArray) {
        val fcc = "00db"
        var size = imageBytes.size
        val fill = size % 4
        if (fill > 0) size += (4 - fill)
        outputStream.apply {
            write(convertData(fcc))
            write(convertData(size))
            write(imageBytes)
            if (fill > 0) {
                for (i in 0 until 4 - fill) write(0)
            }
        }
    }

    fun finish() {
        val fileSize = channel.size()
        channel.position(4L)
        outputStream.write(convertData(fileSize - 8L))
        channel.position(movieListOffset + 4L)
        outputStream.write(convertData(fileSize - movieListOffset - 8L))
        outputStream.close()
    }

    private fun concatData(vararg data: Any): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        byteArrayOutputStream.use {
            data.forEach { byteArrayOutputStream.write(convertData(it)) }
        }
        return byteArrayOutputStream.toByteArray()
    }

    private fun convertData(data: Any): ByteArray {
        return when (data) {
            is Long -> data.to4BytesLittleEndian()
            is Int -> data.to4BytesLittleEndian()
            is Short -> data.toInt().to2BytesLittleEndian()
            is String -> data.toByteArray()
            else -> throw Exception("invalid data type")
        }
    }

    private fun Long.to4BytesLittleEndian(): ByteArray {
        return byteArrayOf(
            (this and 0x000000FF).toByte(),
            (this ushr 8 and 0x000000FF).toByte(),
            (this ushr 16 and 0x000000FF).toByte(),
            (this ushr 24).toByte()
        )
    }

    private fun Int.to4BytesLittleEndian(): ByteArray {
        return byteArrayOf(
            (this and 0x000000FF).toByte(),
            (this ushr 8 and 0x000000FF).toByte(),
            (this ushr 16 and 0x000000FF).toByte(),
            (this ushr 24).toByte()
        )
    }

    private fun Int.to2BytesLittleEndian(): ByteArray {
        return byteArrayOf(
            (this and 0x000000FF).toByte(),
            (this ushr 8).toByte()
        )
    }

}