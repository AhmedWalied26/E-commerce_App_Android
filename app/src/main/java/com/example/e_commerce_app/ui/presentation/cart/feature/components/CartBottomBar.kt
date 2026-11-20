package com.example.e_commerce_app.ui.presentation.cart.feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.Gray
import com.example.e_commerce_app.ui.theme.White
import com.example.e_commerce_app.ui.theme.bold18Blue
import com.example.e_commerce_app.ui.theme.regular14DarkBlue
import com.example.e_commerce_app.ui.theme.semiBold16White

@Composable
fun CartBottomBar(
    modifier: Modifier = Modifier,
    totalPrice: Int,
    itemCount: Int = 0,
    onCheckOutClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = if (itemCount > 0) "Total price ($itemCount items)" else "Total price",
                    style = regular14DarkBlue.copy(color = Gray)
                )
                Text(
                    text = "EGP $totalPrice",
                    style = bold18Blue
                )
            }

            Button(
                onClick = onCheckOutClicked,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue
                ),
                contentPadding = PaddingValues(horizontal = 28.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Check Out",
                        style = semiBold16White
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(R.drawable.arrow_check_out_icon),
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
