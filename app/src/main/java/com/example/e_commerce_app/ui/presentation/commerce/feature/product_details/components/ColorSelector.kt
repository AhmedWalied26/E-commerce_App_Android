package com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.White

@Composable
fun ColorSelector(
    selectedColorIndex: Int,
    onColorSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color(0xFF2E2E2E), // Dark
        Color(0xFFB71C1C), // Red
        Color(0xFF1976D2), // Blue
        Color(0xFF00C853), // Green
        Color(0xFFFF7043)  // Coral
    )
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Color",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            colors.forEachIndexed { index, color ->
                val isSelected = index == selectedColorIndex
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable { onColorSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected color",
                            tint = White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

