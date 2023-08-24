package myplayground.example.dicodingstory.activities.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import myplayground.example.dicodingstory.local_storage.LocalStorageManager
import myplayground.example.dicodingstory.local_storage.model.UserData

class LandingViewModel(private val localStorageManager: LocalStorageManager) : ViewModel() {
    fun getUserData(): LiveData<UserData?> {
        return localStorageManager.getUserDataAsync().asLiveData()
    }
}