package codes.nh.timelapsed.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/*
https://developer.android.com/topic/performance/graphics/cache-bitmap
*/
class ImageLoader(cacheSizePercent: Double = 25.0) {

    private val sizeFactor = (100.0 / cacheSizePercent).toInt()
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / sizeFactor

    private val cache = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            return bitmap.byteCount / 1024
        }
    }

    private val dispatcher = Dispatchers.IO

    private suspend fun loadBitmapFromFile(file: File, sampleSize: Int) = withContext(dispatcher) {
        val options = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
        }
        BitmapFactory.decodeFile(file.path, options)
        /*
        val exif = ExifInterface(file)
        val rotation = exif.rotationDegrees.toFloat()
        if (rotation != 0f) {
            val matrix = Matrix().apply { postRotate(rotation) }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        bitmap
        */
    }

    suspend fun load(file: File, sampleSize: Int): Bitmap {
        val key = file.path
        val cachedBitmap = cache.get(key)
        return if (cachedBitmap == null) {
            val loadedBitmap = loadBitmapFromFile(file, sampleSize)
            cache.put(file.path, loadedBitmap)
            log("loadedBitmap for $key")
            loadedBitmap
        } else {
            log("cachedBitmap for $key")
            cachedBitmap
        }
    }

}