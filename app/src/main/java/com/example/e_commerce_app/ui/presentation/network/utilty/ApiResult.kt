package com.example.e_commerce_app.ui.presentation.network.utilty

import com.example.e_commerce_app.ui.core.errors.AppErrors

sealed interface ApiResult<out T> {

    data class SuccessApiResult<T>(val data: T) : ApiResult<T>
    data class ErrorApiResult(val error: AppErrors) : ApiResult<Nothing>

}