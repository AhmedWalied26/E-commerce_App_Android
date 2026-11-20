package com.example.e_commerce_app.ui.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.White

@Composable
fun CustomAppBar(
    modifier: Modifier = Modifier,
    cartCount: Int = 0,
    showSearch: Boolean = true,
    logoutClicked: () -> Unit,
    cartClicked: () -> Unit = {}
) {
    var search by remember { mutableStateOf("") }
    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Image(
                painter = painterResource(R.drawable.small_route_logo_image),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
            )

            IconButton(onClick = logoutClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Sign Up",
                    tint = Color.Red
                )
            }
        }

        if (showSearch) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CustomTextField(
                    modifier = Modifier.weight(1f),
                    hint = "what do you search for?",
                    value = search,
                    onValueChange = { newValue ->
                        search = newValue
                    },
                    radius = 44,
                    leadingIcon = {
                        IconButton(onClick = {}, modifier = Modifier.padding(start = 8.dp)) {
                            Icon(
                                painter = painterResource(R.drawable.search_icon),
                                contentDescription = null
                            )
                        }
                    })

                IconButton(onClick = cartClicked) {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(
                                    containerColor = Blue,
                                    contentColor = White
                                ) {
                                    Text(text = cartCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.cart_icon),
                            contentDescription = "Cart",
                            tint = DarkBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}