package com.example.e_commerce_app.ui.core.di

import com.example.e_commerce_app.ui.presentation.auth.data.data_source.local.AuthLocalDataSource
import com.example.e_commerce_app.ui.presentation.auth.data.data_source.local.AuthLocalDataSourceImpl
import com.example.e_commerce_app.ui.presentation.auth.data.data_source.remote.AuthRemoteDataSource
import com.example.e_commerce_app.ui.presentation.auth.data.data_source.remote.AuthRemoteDataSourceImpl
import com.example.e_commerce_app.ui.presentation.auth.data.repository.AuthRepositoryImpl
import com.example.e_commerce_app.ui.presentation.auth.domain.repository.AuthRepository
import com.example.e_commerce_app.ui.presentation.cart.data.data_sources.remote.CartRemoteDataSource
import com.example.e_commerce_app.ui.presentation.cart.data.data_sources.remote.CartRemoteDataSourceImpl
import com.example.e_commerce_app.ui.presentation.cart.data.repository.CartRepositoryImpl
import com.example.e_commerce_app.ui.presentation.cart.domain.repository.CartRepository
import com.example.e_commerce_app.ui.presentation.wishList.data.data_sources.remote.WishlistRemoteDataSource
import com.example.e_commerce_app.ui.presentation.wishList.data.data_sources.remote.WishlistRemoteDataSourceImpl
import com.example.e_commerce_app.ui.presentation.wishList.data.repository.WishlistRepositoryImpl
import com.example.e_commerce_app.ui.presentation.wishList.domain.repository.WishlistRepository
import com.example.e_commerce_app.ui.presentation.commerce.data.data_sources.remote.home.HomeRemoteDataSource
import com.example.e_commerce_app.ui.presentation.commerce.data.data_sources.remote.home.HomeRemoteDataSourceImpl
import com.example.e_commerce_app.ui.presentation.commerce.data.repository.HomeRepositoryImpl
import com.example.e_commerce_app.ui.presentation.commerce.domain.repository.home.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    abstract fun bindHomeRemoteDataSource(
        homeRemoteDataSourceImpl: HomeRemoteDataSourceImpl
    ): HomeRemoteDataSource

    @Binds
    abstract fun bindAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindAuthLocalDataSource(
        authLocalDataSourceImpl: AuthLocalDataSourceImpl
    ): AuthLocalDataSource

    @Binds
    abstract fun bindCartRemoteDataSource(
        cartRemoteDataSourceImpl: CartRemoteDataSourceImpl
    ): CartRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository

    @Binds
    abstract fun bindWishlistRemoteDataSource(
        wishlistRemoteDataSourceImpl: WishlistRemoteDataSourceImpl
    ): WishlistRemoteDataSource

    @Binds
    abstract fun bindWishlistRepository(
        wishlistRepositoryImpl: WishlistRepositoryImpl
    ): WishlistRepository
}