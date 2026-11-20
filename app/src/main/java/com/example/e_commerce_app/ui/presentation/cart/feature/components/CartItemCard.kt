package com.example.e_commerce_app.ui.presentation.cart.feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.presentation.cart.domain.entities.CartProductItem
import com.example.e_commerce_app.ui.presentation.common.MainLoadingSection
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.Stroke
import com.example.e_commerce_app.ui.theme.White
import com.example.e_commerce_app.ui.theme.bold18Blue
import com.example.e_commerce_app.ui.theme.medium18DarkBlue
import com.example.e_commerce_app.ui.theme.regular12DarkBlue
import com.example.e_commerce_app.ui.theme.semiBold16White

@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    item: CartProductItem,
    isUpdating: Boolean = false,
    onProductClick: () -> Unit = {},
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Stroke,
                shape = RoundedCornerShape(16.dp)
            )
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image Thumbnail
            Box(
                modifier = Modifier
                    .size(105.dp)
                    .border(
                        width = 1.dp,
                        color = Stroke,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF8F9FA))
                    .clickable { onProductClick() },
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = item.product.imageCover,
                    contentDescription = item.product.title,
                    modifier = Modifier
                        .size(105.dp)
                        .clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        MainLoadingSection()
                    }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(105.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title and Delete Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.product.title,
                        style = medium18DarkBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp),
                        enabled = !isUpdating
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.delete_icon),
                            contentDescription = "Remove Item",
                            tint = DarkBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Attributes Row (Brand / Category)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE25B26))
                    )
                    val attributeText = if (item.product.brandName.isNotEmpty()) {
                        "${item.product.brandName} | Size: 40"
                    } else {
                        "Orange | Size: 40"
                    }
                    Text(
                        text = attributeText,
                        style = regular12DarkBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Price and Quantity Pill Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "EGP ${item.price}",
                        style = bold18Blue
                    )

                    // Quantity Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Blue)
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isUpdating) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(2.dp),
                                color = White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = onDecrement,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.decrement_icon),
                                        contentDescription = "Decrease Quantity",
                                        tint = White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Text(
                                    text = item.count.toString(),
                                    style = semiBold16White
                                )

                                IconButton(
                                    onClick = onIncrement,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.increment_icon),
                                        contentDescription = "Increase Quantity",
                                        tint = White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

