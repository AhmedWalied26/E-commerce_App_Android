package com.example.e_commerce_app.ui.core.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.e_commerce_app.ui.presentation.auth.feature.login.view_model.LoginStates
import com.example.e_commerce_app.ui.presentation.auth.feature.register.view_model.RegisterStates
import com.example.e_commerce_app.ui.presentation.commerce.domain.utility.Resource
import com.example.e_commerce_app.ui.presentation.common.MainLoadingSection
import com.example.e_commerce_app.ui.theme.semiBold20Blue

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    loginState: LoginStates = LoginStates(),
    registerState: RegisterStates = RegisterStates()
) {
    ElevatedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 18.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        if (loginState.loginState is Resource.Loading || registerState.registerState is Resource.Loading)
            MainLoadingSection(

            )
        else
            Text(
                text = text,
                style = semiBold20Blue
            )
    }
}