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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.White

@Composable
fun SizeSelector(
    selectedSize: String,
    onSizeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    sizes: List<String> = listOf("38", "39", "40", "41", "42")
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Size",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            sizes.forEach { size ->
                val isSelected = size == selectedSize
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Blue else Color.Transparent)
                        .clickable { onSizeSelected(size) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = size,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) White else DarkBlue
                    )
                }
            }
        }
    }
}

