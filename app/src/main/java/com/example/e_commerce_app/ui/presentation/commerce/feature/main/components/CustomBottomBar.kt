package com.example.e_commerce_app.ui.presentation.commerce.feature.main.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ui.theme.Blue
import com.example.e_commerce_app.ui.theme.White

@Composable
fun CustomBottomBar(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onClickTab: (Int) -> Unit
) {
    val iconList = listOf(
        R.drawable.selected_home_tab_icon,
        R.drawable.selected_category_tab_icon,
        R.drawable.selected_favourite_tab_icon,
        R.drawable.selected_profile_tab_icon
    )
    NavigationBar(
        containerColor = Blue,
        modifier = Modifier.clip(
            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        iconList.forEachIndexed { index, vector ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Blue,
                    unselectedIconColor = White,
                    indicatorColor = White
                ),
                selected = selectedIndex == index,
                onClick = {
                    onClickTab(index)
                },
                icon = {
                    Icon(
                        painter = painterResource(vector),
                        contentDescription = null
                    )
                })
        }
    }
}