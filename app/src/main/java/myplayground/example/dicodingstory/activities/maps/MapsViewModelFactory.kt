package myplayground.example.dicodingstory.activities.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.dicodingstory.local_storage.LocalStorageManager
import myplayground.example.dicodingstory.network.DicodingStoryApi

@Suppress("UNCHECKED_CAST")
class MapsViewModelFactory(
    private val networkApi: DicodingStoryApi,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(networkApi) as T
        }
        throw IllegalArgumentException("Unknown view class model")
    }
}