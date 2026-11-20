package com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.Gray
import com.example.e_commerce_app.ui.theme.White

@Composable
fun BottomAddToCartSection(
    totalPrice: Int,
    isAddingToCart: Boolean,
    onAddToCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Total price",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "EGP $totalPrice",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        Button(
            onClick = onAddToCartClick,
            enabled = !isAddingToCart,
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = White
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            if (isAddingToCart) {
                CircularProgressIndicator(
                    color = White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_to_cart_icon),
                        contentDescription = "Add to cart",
                        tint = White,
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = "Add to cart",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

