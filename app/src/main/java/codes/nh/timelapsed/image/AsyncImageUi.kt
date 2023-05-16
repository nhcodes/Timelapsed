package codes.nh.timelapsed.image

import androidx.compose.foundation.Image
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import codes.nh.timelapsed.R
import java.io.File


val imageLoader = ImageLoader() //todo

@Composable
fun AsyncImage(
    file: File?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    sampleSize: Int = 4
) {
    val image = remember(file) { mutableStateOf(null as ImageBitmap?) }
    if (file != null) {
        LaunchedEffect(file) {
            image.value = imageLoader.load(file, sampleSize).asImageBitmap()
        }
    }
    val imageValue = image.value
    if (imageValue != null) {
        Image(
            bitmap = imageValue,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.icon_thumbnail),
            contentDescription = contentDescription,
            contentScale = contentScale,
            alpha = 0.5f,
            colorFilter = ColorFilter.tint(color = LocalContentColor.current),
            modifier = modifier
        )
    }
}
