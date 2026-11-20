package com.example.e_commerce_app.ui.presentation.auth.feature.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.core.components.CustomButton
import com.example.e_commerce_app.ui.core.components.CustomText
import com.example.e_commerce_app.ui.core.components.CustomTextField
import com.example.e_commerce_app.ui.presentation.auth.feature.login.view_model.LoginEffects
import com.example.e_commerce_app.ui.presentation.auth.feature.login.view_model.LoginIntent
import com.example.e_commerce_app.ui.presentation.auth.feature.login.view_model.LoginViewModel
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.bold24White
import com.example.e_commerce_app.ui.theme.medium18White
import com.example.e_commerce_app.ui.theme.regular16White

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onCreateAccount: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.states.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effects ->
            when (effects) {
                LoginEffects.OnNavigateToHome -> onLoginClick()
                LoginEffects.OnNavigateToRegister -> onCreateAccount()
                is LoginEffects.ShowToast -> Toast.makeText(
                    context,
                    effects.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Blue)
            .padding(horizontal = 16.dp, vertical = 64.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.route_logo_image),
            contentDescription = null,
            modifier = modifier
                .size(width = 250.dp, height = 100.dp)
                .padding(top = 32.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        CustomText(
            text = "Welcome Back To Route", style = bold24White
        )

        Spacer(modifier = Modifier.height(6.dp))

        CustomText(
            text = "Please sign in with your mail", style = regular16White
        )

        Spacer(modifier = Modifier.height(40.dp))

        CustomText(
            text = "Email", style = regular16White
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = state.email,
            onValueChange = { newValue ->
                viewModel.processIntent(LoginIntent.EmailChanged(newValue))
            },
            hint = "Enter your mail"
        )

        Spacer(modifier = Modifier.height(32.dp))

        CustomText(
            text = "Password", style = regular16White
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = state.password,
            onValueChange = { newValue ->
                viewModel.processIntent(LoginIntent.PasswordChanged(newValue))
            },
            hint = "Enter your Password",
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    painter = painterResource(if (passwordVisible) R.drawable.visibility_on_icon else R.drawable.visibility_off_icon),
                    contentDescription = null,
                    modifier = Modifier.clickable(onClick = {
                        passwordVisible = !passwordVisible
                    })
                )
            })

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {}) {
            Text(
                text = "Forgot password",
                modifier = Modifier.fillMaxWidth(),
                style = regular16White,
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(56.dp))

        CustomButton(
            text = "Login",
            loginState = state,
            onClick = {
                viewModel.processIntent(LoginIntent.OnLoginClicked)
            })

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Don’t have an account?",
                style = medium18White,
            )

            TextButton(
                onClick = {
                    viewModel.processIntent(LoginIntent.OnCreateAccountClicked)
                }
            ) {
                Text(
                    text = "Create Account",
                    style = medium18White,
                )
            }
        }
    }
}