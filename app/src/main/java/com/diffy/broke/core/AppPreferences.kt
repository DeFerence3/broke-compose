package com.diffy.broke.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

private const val PREFERENCE_NAME = "app_preferences"

class AppPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val Context.dataStoreGen: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

    private val dataStore = context.dataStoreGen

    private object Keys {
        val LAST_BACKUP_DATE = longPreferencesKey("last_backup_date")
        val LAST_RESTORE_DATE = longPreferencesKey("last_restore_date")
    }

    val lastBackupDate: Flow<Date?> = dataStore.data.map {
        it[Keys.LAST_BACKUP_DATE]?.let { millis -> Date(millis) }
    }

    suspend fun setLastBackupDate(date: Date) {
        dataStore.edit { preferences ->
            preferences[Keys.LAST_BACKUP_DATE] = date.time
        }
    }

    val lastRestoreDate: Flow<Date?> = dataStore.data.map {
        it[Keys.LAST_RESTORE_DATE]?.let { millis -> Date(millis) }
    }

    suspend fun setLastRestoreDate(date: Date) {
        dataStore.edit { preferences ->
            preferences[Keys.LAST_RESTORE_DATE] = date.time
        }
    }
}