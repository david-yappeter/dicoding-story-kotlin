package myplayground.example.dicodingstory.local_storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import myplayground.example.dicodingstory.local_storage.model.UserData

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dicoding_story_ds")

class DatastoreSettings private constructor(private val dataStore: DataStore<Preferences>) :
    LocalStorageManager {
    companion object {
        private val KEY_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        private val KEY_USER_DATA = stringPreferencesKey("user_data")

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

    override suspend fun saveDarkThemeSettings(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_DARK_THEME] = isDarkTheme
        }
    }

    override fun getDarkThemeSettingsAsync(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[KEY_DARK_THEME] ?: false
        }
    }


    override suspend fun getDarkThemeSettings(): Boolean {
        return getDarkThemeSettingsAsync().first()
    }

    override suspend fun saveUserData(userData: UserData?) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_DATA] = if (userData != null) Gson().toJson(userData) else ""
        }
    }

    override fun getUserDataAsync(): Flow<UserData?> {
        return dataStore.data.map { preferences ->
            val json = preferences[KEY_USER_DATA]
            if (!json.isNullOrEmpty()) {
                return@map Gson().fromJson(json, UserData::class.java)
            } else {
                null
            }
        }
    }

    override suspend fun getUserDataSync(): UserData? {
        return getUserDataAsync().first()
    }
}
