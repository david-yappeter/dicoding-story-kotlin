package myplayground.example.dicodingstory.local_storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import myplayground.example.dicodingstory.local_storage.model.UserData

class SharedPreferencesSettings private constructor(private val sharedPreferences: SharedPreferences) :
    LocalStorageManager {
    companion object {
        private val KEY_DARK_THEME = "is_dark_theme"
        private val KEY_USER_DATA = "user_data"

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

    override suspend fun saveUserData(userData: UserData) {
        sharedPreferences.edit().putString(KEY_USER_DATA, Gson().toJson(userData)).apply()
    }

    override fun getUserDataAsync(): Flow<UserData?> {
        return flow {
            val json = sharedPreferences.getString(KEY_USER_DATA, "")
            if (json != null) {
                emit(Gson().fromJson(json, UserData::class.java))
            } else {
                emit(null)
            }
        }
    }

    override suspend fun getUserDataSync(): UserData? {
        return getUserDataAsync().first()
    }
}
