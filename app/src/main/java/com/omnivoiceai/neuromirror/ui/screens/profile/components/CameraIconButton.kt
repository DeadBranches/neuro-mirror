package com.omnivoiceai.neuromirror.ui.screens.profile.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.omnivoiceai.neuromirror.utils.rememberCameraLauncher

@Composable
fun CameraIconButton(
    onImageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    
    val cameraLauncher = rememberCameraLauncher(
        onPictureTaken = { imageUri -> 
            onImageSelected(imageUri.toString())
        }
    )
    
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onImageSelected(it.toString()) }
    }
    
    IconButton(
        onClick = { showBottomSheet = true },
        modifier = modifier
            .clip(CircleShape)
            .size(32.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            Icons.Outlined.PhotoCamera,
            contentDescription = "Camera icon",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxSize().padding(8.dp)
        )
    }
    
    ImageSelectionBottomSheet(
        showBottomSheet = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        onCameraClick = { cameraLauncher.captureImage() },
        onGalleryClick = { galleryLauncher.launch("image/*") }
    )
} 