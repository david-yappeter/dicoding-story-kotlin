package myplayground.example.dicodingstory.activities.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import myplayground.example.dicodingstory.database.StoryDatabase
import myplayground.example.dicodingstory.database.StoryRemoteMediator
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.network.DicodingStoryApi

@OptIn(ExperimentalPagingApi::class)
class HomeViewModel(
    private val storyDatabase: StoryDatabase,
    networkApi: DicodingStoryApi,
) : ViewModel() {
    private val limit = 6

    val pagerStories: LiveData<PagingData<Story>> = Pager(
        config = PagingConfig(
            pageSize = limit
        ),
        remoteMediator = StoryRemoteMediator(storyDatabase, networkApi),
        pagingSourceFactory = {
            //            StoryPagingSource(networkApi)
            storyDatabase.storyDao().fetchAll()
        },
    ).liveData.cachedIn(viewModelScope)

}