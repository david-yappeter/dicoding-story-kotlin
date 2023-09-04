package myplayground.example.dicodingstory.activities.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.response.ListStoryResponse
import myplayground.example.dicodingstory.network.response.StoryResponse
import myplayground.example.dicodingstory.utils.getOrAwaitValue
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkApi: DicodingStoryApi

    private lateinit var viewModel: HomeViewModel
    private lateinit var mainDispatchers: TestDispatcher

    @Before
    fun setUp() {
        mainDispatchers = UnconfinedTestDispatcher()
        Dispatchers.setMain(mainDispatchers)
        viewModel = HomeViewModel(networkApi)
    }

    @After
    fun tearDown() { // clean up after the tests
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetch stories should not null`() = runTest(mainDispatchers) {
        val pagingData = viewModel.pagerStories.getOrAwaitValue()

        assertNotNull(pagingData)
    }
}
