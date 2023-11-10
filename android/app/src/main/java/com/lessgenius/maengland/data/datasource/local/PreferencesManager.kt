package com.lessgenius.maengland.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lessgenius.maengland.data.repository.AccountRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesManager(context: Context) {
    companion object {
        private val PREFERENCES_NAME = "maengmaeng"
        private val Context.dataStore by preferencesDataStore(
            name = PREFERENCES_NAME
        )
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }

    private val mDataStore: DataStore<Preferences> = context.dataStore


    fun getToken(key: String): Flow<String> {
        return mDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[stringPreferencesKey(key)] ?: ""
            }
    }

    suspend fun updateToken(key: String, value: String) {
        mDataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun remove(key: String) {
        mDataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }
}