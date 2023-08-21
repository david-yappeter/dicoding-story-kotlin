package myplayground.example.dicodingstory.localstorage

import kotlinx.coroutines.flow.Flow

interface LocalStorageManager {
    suspend fun saveDarkThemeSettings(isDarkTheme: Boolean)
    
    suspend fun getDarkThemeSettings(): Boolean
}