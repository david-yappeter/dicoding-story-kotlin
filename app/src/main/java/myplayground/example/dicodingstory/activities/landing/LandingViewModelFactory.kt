package myplayground.example.dicodingstory.activities.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.dicodingstory.local_storage.LocalStorageManager
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class LandingViewModelFactory(
    private val localStorageManager: LocalStorageManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LandingViewModel::class.java)) {
            return LandingViewModel(localStorageManager) as T
        }
        throw IllegalArgumentException("Unknown view class model")
    }
}