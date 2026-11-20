package com.example.e_commerce_app.ui.core.errors

sealed class AppErrors(val errorMessage: String) {
    data class ServerError(val message: String = "Server down please try again later.") :
        AppErrors(errorMessage = message)

    data class NetworkError(val message: String = "No internet connection please try again later.") :
        AppErrors(errorMessage = message)

    data class IgnoredError(val message: String = "") :
        AppErrors(errorMessage = message)

    data class UnknownError(val message: String = "Something went wrong please try again later.") :
        AppErrors(errorMessage = message)
}