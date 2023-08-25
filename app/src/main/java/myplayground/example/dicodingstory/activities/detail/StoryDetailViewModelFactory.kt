package myplayground.example.dicodingstory.activities.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.dicodingstory.network.DicodingStoryApi
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class StoryDetailViewModelFactory(private val networkApi: DicodingStoryApi) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryDetailViewModel::class.java)) {
            return StoryDetailViewModel(networkApi) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}