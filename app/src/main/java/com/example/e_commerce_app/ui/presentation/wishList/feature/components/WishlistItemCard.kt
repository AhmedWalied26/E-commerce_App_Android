package com.example.e_commerce_app.ui.presentation.wishList.feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.presentation.common.MainLoadingSection
import com.example.e_commerce_app.ui.presentation.wishList.domain.entities.WishlistItem
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.DarkBlue
import com.example.e_commerce_app.ui.theme.Stroke
import com.example.e_commerce_app.ui.theme.White
import com.example.e_commerce_app.ui.theme.bold18Blue
import com.example.e_commerce_app.ui.theme.medium18DarkBlue
import com.example.e_commerce_app.ui.theme.regular11Dis
import com.example.e_commerce_app.ui.theme.regular12DarkBlue
import com.example.e_commerce_app.ui.theme.semiBold16White

@Composable
fun WishlistItemCard(
    modifier: Modifier = Modifier,
    item: WishlistItem,
    isRemoving: Boolean = false,
    isAddingToCart: Boolean = false,
    onProductClick: () -> Unit = {},
    onRemoveClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Stroke,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { onProductClick() }
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Cover Thumbnail
            Box(
                modifier = Modifier
                    .size(105.dp)
                    .border(
                        width = 1.dp,
                        color = Stroke,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF8F9FA)),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = item.imageCover,
                    contentDescription = item.title,
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
                // Title and Heart Toggle Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.title,
                        style = medium18DarkBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onRemoveClick,
                        modifier = Modifier.size(32.dp),
                        enabled = !isRemoving
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .shadow(2.dp, CircleShape)
                                .clip(CircleShape)
                                .background(White),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isRemoving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Blue,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.added_to_favourite_icon),
                                    contentDescription = "Remove from Wishlist",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Color / Category Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val dotColor = resolveColor(item.colorPresentation.orEmpty())
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(dotColor)
                    )
                    Text(
                        text = item.colorPresentation ?: item.categoryName ?: "In Stock",
                        style = regular12DarkBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Price and Add to Cart Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val displayPrice = item.priceAfterDiscount ?: item.price
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "EGP $displayPrice",
                            style = bold18Blue
                        )

                        if (item.priceAfterDiscount != null && item.priceAfterDiscount != item.price) {
                            Text(
                                text = "EGP ${item.price}",
                                style = regular11Dis
                            )
                        }
                    }

                    // Add to Cart Button
                    Button(
                        onClick = onAddToCartClick,
                        enabled = !isAddingToCart,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Blue),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        if (isAddingToCart) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Add to Cart",
                                style = semiBold16White.copy(fontSize = 12.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun resolveColor(colorName: String): Color {
    val lower = colorName.lowercase()
    return when {
        lower.contains("black") -> Color(0xFF212121)
        lower.contains("orange") -> Color(0xFFE57E25)
        lower.contains("gold") -> Color(0xFFE6CA97)
        lower.contains("yellow") -> Color(0xFFEFC050)
        lower.contains("blue") -> Blue
        lower.contains("red") || lower.contains("bordeaux") -> Color(0xFFB71C1C)
        lower.contains("green") -> Color(0xFF2E7D32)
        lower.contains("white") -> Color(0xFFEEEEEE)
        lower.contains("pink") -> Color(0xFFE91E63)
        lower.contains("purple") -> Color(0xFF9C27B0)
        lower.contains("brown") -> Color(0xFF795548)
        lower.contains("grey") || lower.contains("gray") || lower.contains("silver") -> Color(0xFF9E9E9E)
        else -> DarkBlue
    }
}

