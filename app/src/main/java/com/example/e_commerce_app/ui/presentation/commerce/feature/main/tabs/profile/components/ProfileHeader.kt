package com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.e_commerce_app.ui.core.components.CustomText
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.Gray
import com.example.e_commerce_app.ui.theme.regular14DarkBlue
import com.example.e_commerce_app.ui.theme.semiBold20Blue

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    userName: String = "Mohamed",
    userEmail: String = "mohamed.N@gmail.com"
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CustomText(
            text = "Welcome, $userName",
            style = semiBold20Blue.copy(color = DarkBlue)
        )

        Spacer(modifier = Modifier.height(4.dp))

        CustomText(
            text = userEmail,
            style = regular14DarkBlue.copy(color = Gray)
        )
    }
}
