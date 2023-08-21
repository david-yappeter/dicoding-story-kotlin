package myplayground.example.dicodingstory.localstorage

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SharedPreferencesSettings private constructor(private val sharedPreferences: SharedPreferences) :
    LocalStorageManager {
    companion object {
        private val KEY_DARK_THEME = "is_dark_theme"

        @Volatile
        private var instance: SharedPreferencesSettings? = null

        fun getInstance(context: Context): SharedPreferencesSettings {
            return instance ?: synchronized(this) {
                val dsSettings = SharedPreferencesSettings(
                    context.getSharedPreferences("dicoding_story_sp", Context.MODE_PRIVATE),
                )
                instance = dsSettings
                return dsSettings
            }
        }
    }

    override suspend fun saveDarkThemePreference(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_THEME, isDarkTheme).apply()
    }

    override suspend fun getDarkThemePreference(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_THEME, false)
    }
}
