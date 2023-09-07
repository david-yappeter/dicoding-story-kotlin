package myplayground.example.dicodingstory.adapter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.response.ListStoryResponse
import myplayground.example.dicodingstory.network.response.StoryResponse
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

/*
    Test reference from this blog:
    https://medium.com/@mohamed.gamal.elsayed/android-how-to-test-paging-3-pagingsource-433251ade028
*/


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PagingSourceTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkApi: DicodingStoryApi

    private lateinit var storyPagingSource: StoryPagingSource
    private lateinit var mainDispatchers: TestDispatcher

    @Before
    fun setUp() {
        mainDispatchers = UnconfinedTestDispatcher()
        Dispatchers.setMain(mainDispatchers)
        storyPagingSource = StoryPagingSource(networkApi)
    }

    @After
    fun tearDown() { // clean up after the tests
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetch stories should not null`() =
        runTest(mainDispatchers) { // Mock whenever user fetch stories with any parameter, will return list of story response
            Mockito.`when`(networkApi.fetchStories(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Response.success(listStoryResponse))

            // expected data from the fetching is limit to only 1 item to make testing easier
            val expectedResult = PagingSource.LoadResult.Page(
                data = listStoryResponse.listStory.map { Story.fromStoryResponse(it) },
                prevKey = -1,
                nextKey = 1,
            )

            val receivedResult = storyPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0, loadSize = 2, placeholdersEnabled = false
                )
            ) as PagingSource.LoadResult.Page<Int, Story>

            // make sure pagingSource list data not null
            assertNotNull(receivedResult.data)

            // check data length
            assertEquals(expectedResult.data.size, receivedResult.data.size)

            // check first item equal
            assertEquals(expectedResult.data[0].id, receivedResult.data[0].id)

            // assert equal expected story response, and the story response from StoryPagingSource
            assertEquals(expectedResult, receivedResult)
        }

    @Test
    fun `when fetch empty stories should return empty array (zero values)`() =
        runTest(mainDispatchers) { // Mock whenever user fetch stories with any parameter, will return list of story response
            Mockito.`when`(networkApi.fetchStories(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Response.success(emptyResponse))

            // expected data from the fetching is limit to only 1 item to make testing easier
            val expectedResult = PagingSource.LoadResult.Page(
                data = emptyResponse.listStory.map { Story.fromStoryResponse(it) },
                prevKey = -1,
                nextKey = null,
            )

            val receivedResult = storyPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0, loadSize = 2, placeholdersEnabled = false
                )
            ) as PagingSource.LoadResult.Page<Int, Story>

            // make sure pagingSource list data not null
            assertNotNull(receivedResult.data)

            // check data length (expectedResult.data.size = 0)
            assertEquals(expectedResult.data.size, receivedResult.data.size)

            // assert equal expected story response, and the story response from StoryPagingSource
            assertEquals(expectedResult, receivedResult)
        }

    companion object {
        private val listStoryResponse = ListStoryResponse(
            false, "OK", listOf(
                StoryResponse(
                    "id-1",
                    "story",
                    "desc",
                    "https://picsum.photos/500/500",
                    "2020-01-01T05:05:05",
                    null,
                    null
                ), StoryResponse(
                    "id-2",
                    "story",
                    "desc",
                    "https://picsum.photos/500/500",
                    "2020-01-01T05:05:05",
                    null,
                    null
                )
            )
        )

        private val emptyResponse = ListStoryResponse(
            false, "OK", listOf()
        )
    }
}
