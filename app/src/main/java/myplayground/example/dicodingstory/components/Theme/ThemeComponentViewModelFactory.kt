package myplayground.example.dicodingstory.activities.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.dicodingstory.localstorage.LocalStorageManager
import java.lang.IllegalArgumentException

class ThemeComponentViewModelFactory(private val localStorageManager: LocalStorageManager): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ThemeComponentViewModel::class.java)) {
            return ThemeComponentViewModel(localStorageManager) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}