package com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerce_app.ui.theme.DarkBlue

@Composable
fun ProductDescriptionSection(
    description: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Description",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.animateContentSize()) {
            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = DarkBlue.copy(alpha = 0.7f),
                lineHeight = 20.sp,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )

            if (description.length > 80) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isExpanded) "Read Less" else "...Read More",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                    modifier = Modifier.clickable { onToggleExpand() }
                )
            }
        }
    }
}

