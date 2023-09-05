package myplayground.example.dicodingstory.activities.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.dicodingstory.database.StoryDatabase
import myplayground.example.dicodingstory.network.DicodingStoryApi
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val storyDatabase: StoryDatabase,
    private val networkApi: DicodingStoryApi,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(storyDatabase, networkApi) as T
        }
        throw IllegalArgumentException("Unknown view class model")
    }
}