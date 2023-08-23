package myplayground.example.dicodingstory.local_storage

import android.content.Context
import android.content.SharedPreferences

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

    override suspend fun saveDarkThemeSettings(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_THEME, isDarkTheme).apply()
    }

    override suspend fun getDarkThemeSettings(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_THEME, false)
    }
}
