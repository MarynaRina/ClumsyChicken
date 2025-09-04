package com.example.clumsychicken.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("cc_prefs")

class HighScoreDataSource(private val context: Context) {
    private val keyHigh = intPreferencesKey("high_score")
    val highScore = context.dataStore.data.map { it[keyHigh] ?: 0 }
    suspend fun setHigh(value: Int) {
        context.dataStore.edit { prefs ->
            val cur = prefs[keyHigh] ?: 0
            if (value > cur) prefs[keyHigh] = value
        }
    }
}