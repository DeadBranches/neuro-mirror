package com.omnivoiceai.neuromirror.ui.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.omnivoiceai.neuromirror.utils.Logger


enum class ImageType { Local, Network }
enum class Size { S, M, Xl, XXL }

@Composable
fun CircleAvatar(image: String, modifier: Modifier = Modifier, type: ImageType = ImageType.Network, size: Size = Size.M, description: String = "Description"){
    Logger.info(image);
    val imageSize = when (size) {
        Size.S -> 32.dp
        Size.M -> 64.dp
        Size.Xl -> 92.dp
        Size.XXL -> 128.dp
        else -> 64.dp
    }
    if(type == ImageType.Local || image.isEmpty()){
        Image(
            Icons.Outlined.Image,
            description,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        )
    } else {
        AsyncImage(
            ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .build(),
            contentDescription = description,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(imageSize)
                .clip(CircleShape)
        )
    }
}
