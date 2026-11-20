package com.example.e_commerce_app.ui.presentation.network.utilty

import com.example.e_commerce_app.ui.core.errors.AppErrors
import com.example.e_commerce_app.ui.presentation.network.models.response.error.CustomResponseError
import com.google.gson.Gson
import okio.IOException
import retrofit2.HttpException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
    try {
        return ApiResult.SuccessApiResult(data = apiCall())
    } catch (e: IOException) {
        return ApiResult.ErrorApiResult(error = AppErrors.NetworkError())
    } catch (e: HttpException) {
        val json = e.response()?.errorBody()?.string()
        val error = Gson().fromJson(json, CustomResponseError::class.java)
        return ApiResult.ErrorApiResult(error = AppErrors.ServerError(message = error.message!!))
    } catch (e: Throwable) {
        val errorMessage = e.localizedMessage ?: e.message ?: "An unexpected error occurred"
        return ApiResult.ErrorApiResult(error = AppErrors.UnknownError(message = errorMessage))
    }
}
