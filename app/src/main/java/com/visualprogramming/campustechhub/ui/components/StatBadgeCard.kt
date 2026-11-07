package com.visualprogramming.campustechhub.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.visualprogramming.campustechhub.ui.theme.BlueLight
import com.visualprogramming.campustechhub.ui.theme.BluePrimary
import com.visualprogramming.campustechhub.ui.theme.Slate200
import com.visualprogramming.campustechhub.ui.theme.Slate500
import com.visualprogramming.campustechhub.ui.theme.Slate800
import com.visualprogramming.campustechhub.ui.theme.SoftPurple
import com.visualprogramming.campustechhub.ui.theme.SoftPurpleLight

@Composable
fun StatBadgeRow(
    joinedClubsCount: Int,
    totalProjectsCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatBadgeCard(
            count = joinedClubsCount,
            title = "Joined Clubs",
            subtitle = "Komunitas yang Diikuti",
            icon = Icons.Default.Group,
            iconTint = BluePrimary,
            iconBg = BlueLight,
            modifier = Modifier.weight(1f)
        )

        StatBadgeCard(
            count = totalProjectsCount,
            title = "Total Projects",
            subtitle = "Seluruh Proyek Dibuat",
            icon = Icons.Default.Layers,
            iconTint = SoftPurple,
            iconBg = SoftPurpleLight,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatBadgeCard(
    count: Int,
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = count.toString(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Slate800
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate800
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Slate500
            )
        }
    }
}

