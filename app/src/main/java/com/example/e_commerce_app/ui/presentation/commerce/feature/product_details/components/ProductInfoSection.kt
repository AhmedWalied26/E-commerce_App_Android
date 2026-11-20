package com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products.ProductItem
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.Stroke

@Composable
fun ProductInfoSection(
    product: ProductItem,
    selectedQuantity: Int,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
    modifier: Modifier = Modifier
) {
    val price = product.priceAfterDiscount ?: product.price ?: 0
    val maxStock = product.quantity ?: Int.MAX_VALUE
    val soldCount = product.sold ?: 3230
    val ratingsAvg = product.ratingsAverage?.toString() ?: "4.8"
    val ratingsCount = product.ratingsQuantity ?: 7500

    Column(modifier = modifier.fillMaxWidth()) {
        // Row 1: Title and Price
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.title.orEmpty(),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkBlue,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "EGP $price",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 2: Sold Count, Rating, and Quantity Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sold Count Pill
                Box(
                    modifier = Modifier
                        .border(width = 1.dp, color = Stroke, shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "$soldCount Sold",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkBlue
                    )
                }

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.rating_icon),
                        contentDescription = "Rating",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = "$ratingsAvg ($ratingsCount)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkBlue
                    )
                }
            }

            // Quantity Selector
            QuantitySelector(
                quantity = selectedQuantity,
                onIncrement = onIncrementQuantity,
                onDecrement = onDecrementQuantity,
                canIncrement = selectedQuantity < maxStock,
                canDecrement = selectedQuantity > 1
            )
        }
    }
}

