package com.example.e_commerce_app.ui.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.e_commerce_app.ui.theme.bold18Blue

@Composable
fun MainErrorSection(
    modifier: Modifier = Modifier,
    onTryAgain: () -> Unit,
    errorMessage: String,
    buttonText: String = "Try again"
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = errorMessage,
                style = bold18Blue,
                textAlign = TextAlign.Center
            )

            TextButton(onClick = onTryAgain) {
                Text(text = buttonText, style = bold18Blue)
            }
        }
    }
}