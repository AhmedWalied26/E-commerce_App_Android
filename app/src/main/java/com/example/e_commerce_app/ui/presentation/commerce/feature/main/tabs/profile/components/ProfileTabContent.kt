package com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.core.components.CustomText
import com.example.e_commerce_app.ui.core.components.CustomTextField
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.medium18DarkBlue

@Composable
fun ProfileTabContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 8.dp, bottom = 24.dp)
    ) {
        ProfileHeader()

        Spacer(modifier = Modifier.height(24.dp))

        CustomText(
            text = "Your full name",
            style = medium18DarkBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomTextField(
            value = "Mohamed Mohamed Nabil",
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.edit_icon),
                    contentDescription = "Edit your full name",
                    tint = DarkBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = "Your E-mail",
            style = medium18DarkBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomTextField(
            value = "mohamed.N@gmail.com",
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.edit_icon),
                    contentDescription = "Edit your email",
                    tint = DarkBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = "Your password",
            style = medium18DarkBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomTextField(
            value = "*********************",
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.edit_icon),
                    contentDescription = "Edit your password",
                    tint = DarkBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = "Your mobile number",
            style = medium18DarkBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomTextField(
            value = "01122118855",
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.edit_icon),
                    contentDescription = "Edit your mobile number",
                    tint = DarkBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = "Your Address",
            style = medium18DarkBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomTextField(
            value = "6th October, street 11.....",
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.edit_icon),
                    contentDescription = "Edit your address",
                    tint = DarkBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    }
}
