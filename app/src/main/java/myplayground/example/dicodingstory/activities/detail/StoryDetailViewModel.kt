package myplayground.example.dicodingstory.activities.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.network.DicodingStoryApi

class StoryDetailViewModel(private val networkApi: DicodingStoryApi) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val storyDetail = MutableLiveData<Story>()

    fun fetchStory(id: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                isLoading.value = true
            }

            try {
                val response = networkApi.getStory(id)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        storyDetail.postValue(Story.fromStoryResponse(body.story!!))
                        isLoading.postValue(false)
                    }
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