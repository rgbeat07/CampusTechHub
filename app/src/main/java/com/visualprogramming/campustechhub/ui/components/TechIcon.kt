package com.visualprogramming.campustechhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.visualprogramming.campustechhub.ui.theme.BlueLight
import com.visualprogramming.campustechhub.ui.theme.BluePrimary
import com.visualprogramming.campustechhub.ui.theme.SoftGreen
import com.visualprogramming.campustechhub.ui.theme.SoftGreenLight
import com.visualprogramming.campustechhub.ui.theme.SoftPurple
import com.visualprogramming.campustechhub.ui.theme.SoftPurpleLight
import com.visualprogramming.campustechhub.ui.theme.SoftRed
import com.visualprogramming.campustechhub.ui.theme.SoftRedLight

@Composable
fun TechIcon(
    iconType: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    iconSize: Dp = 26.dp
) {
    val (bgColor, iconColor, imageVector) = when (iconType.lowercase()) {
        "android" -> Triple(BlueLight, BluePrimary, Icons.Default.Android)
        "security" -> Triple(SoftRedLight, SoftRed, Icons.Default.Security)
        "ai" -> Triple(SoftPurpleLight, SoftPurple, Icons.Default.AutoAwesome)
        "mobile" -> Triple(BlueLight, BluePrimary, Icons.Default.PhoneAndroid)
        "cloud" -> Triple(SoftPurpleLight, SoftPurple, Icons.Default.Cloud)
        "web" -> Triple(SoftGreenLight, SoftGreen, Icons.Default.Language)
        else -> Triple(BlueLight, BluePrimary, Icons.Default.Code)
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = iconType,
            tint = iconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}

