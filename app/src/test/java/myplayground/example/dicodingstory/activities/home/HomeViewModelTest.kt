package myplayground.example.dicodingstory.activities.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import myplayground.example.dicodingstory.adapter.StoryListAdapter
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.repository.StoryRepository
import myplayground.example.dicodingstory.utils.DummyData
import myplayground.example.dicodingstory.utils.getOrAwaitValue
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)

class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mainDispatchers: TestDispatcher

    @Before
    fun setUp() {
        mainDispatchers = UnconfinedTestDispatcher()
        Dispatchers.setMain(mainDispatchers)
    }

    @After
    fun tearDown() { // clean up after the tests
        Dispatchers.resetMain()
}

    @Test
    fun `when fetch stories should not null`() =
        runTest(mainDispatchers) {

            val dummyStory = DummyData.generateDummyStoryResponse()
            val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStory)
            val expectedQuote = MutableLiveData<PagingData<Story>>()
            expectedQuote.value = data
            Mockito.`when`(storyRepository.getStoryPager()).thenReturn(expectedQuote)

            val viewModel = HomeViewModel(storyRepository)
            val actualStory = viewModel.pagerStories.getOrAwaitValue()

            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryListAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main,
            )
            differ.submitData(actualStory)

            // assert not null
            assertNotNull(differ.snapshot())
            // assert size
            assertEquals(dummyStory.size, differ.snapshot().size)
            // assert first story
            assertEquals(dummyStory[0], differ.snapshot()[0])
        }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<Story>>()
        expectedQuote.value = data
        Mockito.`when`(storyRepository.getStoryPager()).thenReturn(expectedQuote)
        val viewModel = HomeViewModel(storyRepository)
        val actualQuote: PagingData<Story> = viewModel.pagerStories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        // check data is empty
        assertEquals(0, differ.snapshot().size)
    }
}

private class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}