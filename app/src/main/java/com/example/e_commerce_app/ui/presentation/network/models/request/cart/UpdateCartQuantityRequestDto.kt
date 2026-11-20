package com.example.e_commerce_app.ui.presentation.network.models.request.cart

import com.google.gson.annotations.SerializedName

data class UpdateCartQuantityRequestDto(
    @field:SerializedName("count")
    val count: String
) {
    constructor(count: Int) : this(count.toString())
}

