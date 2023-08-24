package myplayground.example.dicodingstory.activities.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.network.DicodingStoryApi

class HomeViewModel(private val networkApi: DicodingStoryApi) : ViewModel() {
    val stories = MutableLiveData<List<Story>>()
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    private val backgroundExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled ${throwable.localizedMessage}")
    }

    fun fetchStories() {
        viewModelScope.launch(Dispatchers.IO + backgroundExceptionHandler) {
            withContext(Dispatchers.Main) {
                isLoading.value = true
            }

            try {
                val response = networkApi.fetchStories()

                if (response.isSuccessful) {
                    val body = response.body()
                    isLoading.postValue(false)
                    stories.postValue(body?.listStory?.map { Story.fromStoryResponse(it) })
                } else {
                    onError("Error: ${response.message()}")
                }
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        isLoading.postValue(false)
    }
}