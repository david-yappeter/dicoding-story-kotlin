package myplayground.example.dicodingstory.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import myplayground.example.dicodingstory.database.StoryDatabase
import myplayground.example.dicodingstory.database.StoryRemoteMediator
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.network.DicodingStoryApi

@OptIn(ExperimentalPagingApi::class)
class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val networkApi: DicodingStoryApi
) {
    private val limit = 6

    fun getStoryPager(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = limit
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, networkApi),
            pagingSourceFactory = {
                //            StoryPagingSource(networkApi)
                storyDatabase.storyDao().fetchAll()
            },
        ).liveData
    }
}