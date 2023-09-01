package myplayground.example.dicodingstory.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.network.DicodingStoryApi

class StoryPagingSource(private val networkApi: DicodingStoryApi) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = networkApi.fetchStories(page, params.loadSize)
            if (responseData.body() == null) {
                error("Response Error")
            }

            val stories = responseData.body()!!.listStory.map { Story.fromStoryResponse(it) }
            return LoadResult.Page(
                data = stories,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (stories.isEmpty()) null else page + 1,
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}