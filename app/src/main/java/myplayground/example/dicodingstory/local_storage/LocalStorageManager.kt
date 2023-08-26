package myplayground.example.dicodingstory.local_storage

import kotlinx.coroutines.flow.Flow
import myplayground.example.dicodingstory.local_storage.model.UserData

interface LocalStorageManager {
    suspend fun saveDarkThemeSettings(isDarkTheme: Boolean)

    suspend fun getDarkThemeSettings(): Boolean

    suspend fun saveUserData(userData: UserData?)

    fun getUserDataAsync(): Flow<UserData?>

    suspend fun getUserDataSync(): UserData?
}