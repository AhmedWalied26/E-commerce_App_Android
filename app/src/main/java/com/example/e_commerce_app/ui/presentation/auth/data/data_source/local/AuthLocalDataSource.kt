package com.example.e_commerce_app.ui.presentation.auth.data.data_source.local

import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    //todo get token
    val tokenFlow : Flow<String?>

    //todo save token
    suspend fun saveToken(token : String)

    //todo remove token
    suspend fun removeToken()

    //todo first
    val isFirstTime : Flow<Boolean>

    //todo edit first time => false
    suspend fun setFirstTimeCompleted()
}