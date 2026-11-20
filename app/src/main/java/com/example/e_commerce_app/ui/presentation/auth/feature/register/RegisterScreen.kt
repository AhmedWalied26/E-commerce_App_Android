package com.example.e_commerce_app.ui.presentation.auth.feature.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.core.components.CustomButton
import com.example.e_commerce_app.ui.core.components.CustomText
import com.example.e_commerce_app.ui.core.components.CustomTextField
import com.example.e_commerce_app.ui.presentation.auth.feature.register.view_model.RegisterEffects
import com.example.e_commerce_app.ui.presentation.auth.feature.register.view_model.RegisterIntent
import com.example.e_commerce_app.ui.presentation.auth.feature.register.view_model.RegisterViewModel
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.regular16White

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.states.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var rePasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effects ->
            when (effects) {
                RegisterEffects.OnNavigateToHome -> onSignUpClick()
                is RegisterEffects.ShowToast -> Toast.makeText(
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

        Spacer(modifier = Modifier.height(40.dp))

        CustomText(
            text = "Full Name", style = regular16White
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = state.name,
            onValueChange = { newValue ->
                viewModel.processIntent(RegisterIntent.NameChanged(newValue))
            }, hint = "Enter your name", supportingText = {
                state.nameError?.let {
                    Text(
                        text = it, color = Color.Red
                    )
                }
            }, errorMessage = state.nameError
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = "Mobile Number", style = regular16White
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = state.phone,
            onValueChange = { newValue ->
                viewModel.processIntent(RegisterIntent.PhoneChanged(newValue))
            },
            hint = "Enter your phone",
            errorMessage = state.phoneError,
            supportingText = {
                state.phoneError?.let {
                    Text(
                        text = it, color = Color.Red
                    )
                }
            })

        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = "E-mail address", style = regular16White
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = state.email,
            onValueChange = { newValue ->
                viewModel.processIntent(RegisterIntent.EmailChanged(newValue))
            },
            hint = "Enter your mail",
            errorMessage = state.emailError,
            supportingText = {
                state.emailError?.let {
                    Text(
                        text = it, color = Color.Red
                    )
                }
            })

        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = "Password", style = regular16White
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = state.password,
            onValueChange = { newValue ->
                viewModel.processIntent(RegisterIntent.PasswordChanged(newValue))
            },
            hint = "Enter your password",
            errorMessage = state.passwordError,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            supportingText = {
                state.passwordError?.let {
                    Text(
                        text = it, color = Color.Red
                    )
                }
            },

            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable(onClick = {
                        passwordVisible = !passwordVisible
                    }),
                    painter = painterResource(if (passwordVisible) R.drawable.visibility_on_icon else R.drawable.visibility_off_icon),
                    contentDescription = null
                )
            })

        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = "Confirm Password", style = regular16White
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = state.rePassword,
            onValueChange = { newValue ->
                viewModel.processIntent(RegisterIntent.RePasswordChanged(newValue))
            },
            hint = "Enter your confirm password",
            visualTransformation = if (rePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            errorMessage = state.rePasswordError,
            supportingText = {
                state.rePasswordError?.let {
                    Text(
                        text = it, color = Color.Red
                    )
                }
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable(onClick = {
                        rePasswordVisible = !rePasswordVisible
                    }),
                    painter = painterResource(if (rePasswordVisible) R.drawable.visibility_on_icon else R.drawable.visibility_off_icon),
                    contentDescription = null
                )
            })

        Spacer(modifier = Modifier.height(56.dp))

        CustomButton(
            registerState = state,
            text = "Sign up",
            onClick = {
                //todo navigate to home
                viewModel.processIntent(RegisterIntent.OnSignUpClicked)
            })

        Spacer(modifier = Modifier.height(56.dp))
    }
}