package com.example.e_commerce_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.e_commerce_app.ui.core.navigation.AppNavigation
import com.example.e_commerce_app.ui.theme.ECommerce_AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.startDestination.value == null
        }
        enableEdgeToEdge()
        setContent {
            ECommerce_AppTheme {
                val startDestination by mainViewModel.startDestination.collectAsStateWithLifecycle()
                startDestination?.let { destination ->
                    AppNavigation(
                        startDestination = destination,
                        onSplashFinished = { mainViewModel.onSplashFinished() }
                    )
                }
            }
        }
    }
}
