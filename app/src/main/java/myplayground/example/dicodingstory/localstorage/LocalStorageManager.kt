package myplayground.example.dicodingstory.localstorage

import kotlinx.coroutines.flow.Flow

interface LocalStorageManager {
    suspend fun saveDarkThemePreference(isDarkTheme: Boolean)
    
    suspend fun getDarkThemePreference(): Boolean
}