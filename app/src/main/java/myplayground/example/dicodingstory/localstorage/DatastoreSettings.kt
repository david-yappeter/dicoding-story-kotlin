package myplayground.example.dicodingstory.localstorage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dicoding_story_ds")

class DatastoreSettings private constructor(private val dataStore: DataStore<Preferences>) :
    LocalStorageManager {
    companion object {
        private val KEY_DARK_THEME = booleanPreferencesKey("is_dark_theme")

        @Volatile
        private var instance: DatastoreSettings? = null

        fun getInstance(ds: DataStore<Preferences>): DatastoreSettings {
            return instance ?: synchronized(this) {
                val dsSettings = DatastoreSettings(ds)
                instance = dsSettings
                return dsSettings
            }
        }
    }

    override suspend fun saveDarkThemePreference(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_DARK_THEME] = isDarkTheme
        }
    }

    override suspend fun getDarkThemePreference(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[KEY_DARK_THEME] ?: false
        }.first()
    }
}
