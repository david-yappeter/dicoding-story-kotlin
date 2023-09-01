package myplayground.example.dicodingstory.activities.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import myplayground.example.dicodingstory.adapter.StoryPagingSource
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.network.DicodingStoryApi

class HomeViewModel(private val networkApi: DicodingStoryApi) : ViewModel() {
    val stories = MutableLiveData<List<Story>>(listOf())
    val appendStories = MutableLiveData<List<Story>>(listOf())
    val isLastPage = MutableLiveData(false)
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData(false)
    val currentPage = MutableLiveData(1)
    private val limit = 6
    private val backgroundExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled ${throwable.localizedMessage}")
    }

    val pagerStories: LiveData<PagingData<Story>> = Pager(
        config = PagingConfig(
            pageSize = 6
        ),
        pagingSourceFactory = {
            StoryPagingSource(networkApi)
        }
    ).liveData.cachedIn(viewModelScope)

    fun fetchStories(isLoadMore: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO + backgroundExceptionHandler) {
            withContext(Dispatchers.Main) {
                if (!isLoadMore) {
                    isLoading.value = true
                }
            }

            try {
                val response = networkApi.fetchStories(currentPage.value, limit)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.listStory?.size != limit) {
                        isLastPage.postValue(true)
                    }

                    currentPage.postValue((currentPage.value as Int) + 1)
                    isLoading.postValue(false)
                    if (isLoadMore) {
                        appendStories.postValue(body?.listStory?.map { Story.fromStoryResponse(it) })
                    } else {
                        stories.postValue(body?.listStory?.map { Story.fromStoryResponse(it) })
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