package com.example.e_commerce_app.ui.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CustomText(modifier: Modifier = Modifier, text: String, style: TextStyle) {
    Text(
        text = text, style = style,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start
    )
}