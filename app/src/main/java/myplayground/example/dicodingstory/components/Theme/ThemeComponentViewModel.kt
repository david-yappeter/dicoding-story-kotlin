package myplayground.example.dicodingstory.activities.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import myplayground.example.dicodingstory.local_storage.LocalStorageManager

class ThemeComponentViewModel(private val localStorageManager: LocalStorageManager): ViewModel() {
    fun getDarkThemeSettings(): Boolean {
        return  runBlocking {
            localStorageManager.getDarkThemeSettings()
        }
    }

    fun setIsDarkThemeSettings(isDarkTheme: Boolean) {
        viewModelScope.launch {
            localStorageManager.saveDarkThemeSettings(isDarkTheme)
        }
    }
}