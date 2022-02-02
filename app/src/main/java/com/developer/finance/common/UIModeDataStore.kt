package com.developer.finance.common

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UIModeDataStore(context: Context): UIModeImpl {
    private val Context.themePrefDataStore by preferencesDataStore("ui_mode_pref")

    private val dataStore = context.themePrefDataStore

    override val isDarkMode: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            val uiMode = preferences[booleanPreferencesKey("ui_mode_pref")] ?: false
            uiMode
        }

    override suspend fun saveToDataStore(isNightMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(("ui_mode_pref"))] = isNightMode
        }
    }

}

interface UIModeImpl {
    val isDarkMode: Flow<Boolean>
    suspend fun saveToDataStore(isNightMode: Boolean)
}
