package com.example.e_commerce_app.ui.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.PlaceHolderColor
import com.example.e_commerce_app.ui.theme.Stroke
import com.example.e_commerce_app.ui.theme.White
import com.example.e_commerce_app.ui.theme.light14Hint
import com.example.e_commerce_app.ui.theme.regular14DarkBlue

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    hint: String = "",
    value: String,
    onValueChange: (String) -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    radius: Int = 16,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    readOnly: Boolean = false,
    textStyle: TextStyle = regular14DarkBlue,
    borderColor: Color = Stroke,
) {

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),

        value = value,
        onValueChange = onValueChange,

        readOnly = readOnly,

        singleLine = true,

        textStyle = textStyle,

        isError = errorMessage != null,

        supportingText = supportingText,

        visualTransformation = visualTransformation,

        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = DarkBlue,
            unfocusedTextColor = DarkBlue,
            disabledTextColor = DarkBlue,
            focusedBorderColor = borderColor,
            focusedContainerColor = White,
            focusedPlaceholderColor = PlaceHolderColor,
            unfocusedBorderColor = borderColor,
            unfocusedContainerColor = White,
            unfocusedPlaceholderColor = PlaceHolderColor,
            errorBorderColor = Color.Red,
            errorContainerColor = White,
            errorPlaceholderColor = PlaceHolderColor,
            disabledBorderColor = borderColor,
            disabledContainerColor = White,
            disabledPlaceholderColor = PlaceHolderColor,
        ),

        placeholder = {
            if (hint.isNotEmpty()) {
                Text(
                    text = hint,
                    style = light14Hint,
                )
            }
        },

        trailingIcon = trailingIcon,

        leadingIcon = leadingIcon,

        shape = RoundedCornerShape(radius)
    )
}