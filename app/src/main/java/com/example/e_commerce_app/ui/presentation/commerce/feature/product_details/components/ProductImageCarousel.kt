package com.example.e_commerce_app.ui.presentation.commerce.feature.product_details.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.presentation.common.MainLoadingSection
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.Stroke
import com.example.e_commerce_app.ui.theme.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImageCarousel(
    images: List<String>,
    isFavorite: Boolean,
    isFavoriteLoading: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayImages = if (images.isNotEmpty()) images else listOf("")
    val pagerState = rememberPagerState(pageCount = { displayImages.size })

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .border(width = 1.dp, color = Stroke, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(White)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            SubcomposeAsyncImage(
                model = displayImages.getOrNull(page),
                contentDescription = "Product Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = { MainLoadingSection() },
                error = {
                    Icon(
                        painter = painterResource(R.drawable.small_route_logo_image),
                        contentDescription = "Image unavailable",
                        tint = Stroke,
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                    )
                }
            )
        }

        // Floating Wishlist Heart Icon
        IconButton(
            onClick = onFavoriteClick,
            enabled = !isFavoriteLoading,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(White)
                    .border(width = 1.dp, color = Stroke, shape = CircleShape),
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
                        tint = Color.Unspecified,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Pager indicators
        if (displayImages.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(displayImages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(width = 24.dp, height = 6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Blue)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .border(width = 1.dp, color = Blue, shape = CircleShape)
                        )
                    }
                }
            }
        }
    }
}

