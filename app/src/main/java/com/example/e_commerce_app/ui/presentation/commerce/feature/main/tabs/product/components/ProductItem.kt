package com.example.e_commerce_app.ui.presentation.commerce.feature.main.tabs.product.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.presentation.commerce.domain.entities.products.ProductItem
import com.example.e_commerce_app.ui.presentation.common.MainLoadingSection
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.Stroke
import com.example.e_commerce_app.ui.theme.White
import com.example.e_commerce_app.ui.theme.Yellow
import com.example.e_commerce_app.ui.theme.regular11Dis
import com.example.e_commerce_app.ui.theme.regular14DarkBlue

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: ProductItem,
    isFavorite: Boolean = false,
    isFavoriteLoading: Boolean = false,
    onProductClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onAddToCartClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp, color = Stroke, shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { onProductClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier, verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier
            ) {
                SubcomposeAsyncImage(
                    model = product.imageCover,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth().height(170.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.FillWidth,
                    loading = {
                        MainLoadingSection()
                    },
                )
                IconButton(
                    onClick = onFavoriteClick,
                    enabled = !isFavoriteLoading
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(White),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isFavoriteLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Blue,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                painter = painterResource(
                                    if (isFavorite) R.drawable.added_to_favourite_icon
                                    else R.drawable.selected_favourite_tab_icon
                                ),
                                contentDescription = if (isFavorite) "Remove from wishlist" else "Add to wishlist",
                                tint = androidx.compose.ui.graphics.Color.Unspecified,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
            Text(
                text = product.title ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = regular14DarkBlue,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Text(
                text = product.description ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = regular14DarkBlue,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            val displayPrice = product.priceAfterDiscount ?: product.price ?: 0.0
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "EGP $displayPrice", style = regular14DarkBlue
                )

                if (product.priceAfterDiscount != null && product.price != null && product.price != product.priceAfterDiscount)
                    Text(
                        text = "EGP ${product.price}", style = regular11Dis
                    )
            }

            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Review (${product.ratingsAverage})", style = regular14DarkBlue
                )

                Icon(
                    painterResource(R.drawable.rating_icon),
                    contentDescription = null,
                    tint = Yellow
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = onAddToCartClick,
                    modifier = Modifier.size(
                        height = 50.dp,
                        width = 30.dp
                    )
                ) {
                    Icon(
                        painterResource(R.drawable.add_to_cart_filled),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                    )
                }
            }
        }
    }
}