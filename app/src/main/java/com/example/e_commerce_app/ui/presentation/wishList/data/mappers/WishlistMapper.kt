package com.example.e_commerce_app.ui.presentation.wishList.data.mappers

import com.example.e_commerce_app.ui.presentation.network.models.response.products.ProductItemDto
import com.example.e_commerce_app.ui.presentation.wishList.domain.entities.WishlistItem

fun ProductItemDto.toWishlistItem(): WishlistItem {
    val rawDescription = description.orEmpty()
    val extractedColor = extractColorPresentation(rawDescription, title.orEmpty())

    val ratingsAvg = when (val avg = ratingsAverage) {
        is Number -> avg.toDouble()
        is String -> avg.toDoubleOrNull() ?: 0.0
        else -> 0.0
    }

    return WishlistItem(
        id = id.orEmpty(),
        title = title.orEmpty(),
        description = rawDescription,
        price = price ?: 0,
        priceAfterDiscount = priceAfterDiscount,
        imageCover = imageCover.orEmpty(),
        ratingsAverage = ratingsAvg,
        categoryName = category?.name,
        brandName = brand?.name,
        colorPresentation = extractedColor ?: category?.name ?: "Standard"
    )
}

private fun extractColorPresentation(description: String, title: String): String? {
    // 1. Check for "Colour Name\t<Color>" in description
    val colorRegex = Regex("""Colour Name\s*[:\t]\s*([A-Za-z0-9\s]+)""", RegexOption.IGNORE_CASE)
    colorRegex.find(description)?.groupValues?.getOrNull(1)?.trim()?.let {
        val firstWord = it.lines().firstOrNull()?.trim() ?: it
        return "$firstWord color"
    }

    // 2. Check for common color words in title or description
    val commonColors = listOf(
        "Black", "White", "Orange", "Gold", "Blue", "Red", "Green",
        "Yellow", "Pink", "Purple", "Bordeaux", "Brown", "Grey", "Gray", "Silver"
    )
    for (color in commonColors) {
        if (title.contains(color, ignoreCase = true) || description.contains(color, ignoreCase = true)) {
            return if (color.equals("Gold", ignoreCase = true) && (title.contains("Watch", ignoreCase = true) || description.contains("Watch", ignoreCase = true))) {
                "Gold Watch"
            } else {
                "$color color"
            }
        }
    }

    return null
}

