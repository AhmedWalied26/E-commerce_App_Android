package com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.White

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    canIncrement: Boolean = true,
    canDecrement: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Blue)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = onDecrement,
            enabled = canDecrement,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.decrement_icon),
                contentDescription = "Decrement quantity",
                tint = if (canDecrement) Color.Unspecified else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = quantity.toString(),
            color = White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        IconButton(
            onClick = onIncrement,
            enabled = canIncrement,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.increment_icon),
                contentDescription = "Increment quantity",
                tint = if (canIncrement) Color.Unspecified else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

