package myplayground.example.dicodingstory.components.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import myplayground.example.dicodingstory.local_storage.LocalStorageManager

class ThemeComponentViewModel(private val localStorageManager: LocalStorageManager) : ViewModel() {
    fun getDarkThemeSettingsAsync(): LiveData<Boolean> {
        return localStorageManager.getDarkThemeSettingsAsync().asLiveData()
    }
}