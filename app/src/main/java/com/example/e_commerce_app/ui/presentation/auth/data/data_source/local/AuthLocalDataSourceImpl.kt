package com.example.e_commerce_app.ui.presentation.auth.data.data_source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class AuthLocalDataSourceImpl @Inject constructor(@ApplicationContext private val context: Context) :
    AuthLocalDataSource {

    private val TOKEN_KEY = stringPreferencesKey(name = "Token")
    private val IS_FIRST_TIME_KEY = booleanPreferencesKey(name = "isFirstTime")

    override val tokenFlow: Flow<String?>
        get() = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[TOKEN_KEY]
            }

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun removeToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    override val isFirstTime: Flow<Boolean>
        get() = context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[IS_FIRST_TIME_KEY] ?: true
        }

    override suspend fun setFirstTimeCompleted() {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME_KEY] = false
        }
    }
}