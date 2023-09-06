package myplayground.example.dicodingstory.activities.add_story

import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.dicodingstory.network.DicodingStoryApi

@Suppress("UNCHECKED_CAST")
class AddStoryViewModelFactory(
    private val networkApi: DicodingStoryApi,
    private val locationManager: LocationManager
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(networkApi, locationManager) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}