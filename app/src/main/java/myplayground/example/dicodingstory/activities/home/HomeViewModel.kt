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
    private val limit = 6

    val pagerStories: LiveData<PagingData<Story>> = Pager(
        config = PagingConfig(
            pageSize = limit
        ),
        pagingSourceFactory = {
            StoryPagingSource(networkApi)
        },
    ).liveData.cachedIn(viewModelScope)

}