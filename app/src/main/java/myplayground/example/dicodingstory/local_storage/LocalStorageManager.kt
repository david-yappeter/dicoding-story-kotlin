package myplayground.example.dicodingstory.local_storage

interface LocalStorageManager {
    suspend fun saveDarkThemeSettings(isDarkTheme: Boolean)
    
    suspend fun getDarkThemeSettings(): Boolean
}