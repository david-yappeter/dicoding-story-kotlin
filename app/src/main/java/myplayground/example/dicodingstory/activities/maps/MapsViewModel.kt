package myplayground.example.dicodingstory.activities.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import myplayground.example.dicodingstory.local_storage.LocalStorageManager
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.network.DicodingStoryApi

class MapsViewModel(
    private val networkApi: DicodingStoryApi,
) : ViewModel() {
    val stories = MutableLiveData<List<Story>>(listOf())
    val errorMessage = MutableLiveData<String>()
    val isSuccess = MutableLiveData<Boolean>(false)
    val isLoading = MutableLiveData<Boolean>(false)
    private val backgroundExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.localizedMessage?.let { onError(it) }
    }

    fun fetchStories() {
        viewModelScope.launch(Dispatchers.IO + backgroundExceptionHandler) {
            withContext(Dispatchers.Main) {
                isLoading.value = true
            }

            try {
                val response = networkApi.fetchStories(null, null, 1)
                if (response.isSuccessful) {
                    val body = response.body()

                    body?.listStory?.let { storiesResponse ->
                        stories.postValue(storiesResponse.map { Story.fromStoryResponse(it) })
                    }
                    isLoading.postValue(false)
                    isSuccess.postValue(true)
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
        isSuccess.postValue(false)
        isLoading.postValue(false)
    }
}