package myplayground.example.dicodingstory.activities.add_story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.dicodingstory.network.DicodingStoryApi
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class AddStoryViewModelFactory(private val networkApi: DicodingStoryApi) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(networkApi) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}