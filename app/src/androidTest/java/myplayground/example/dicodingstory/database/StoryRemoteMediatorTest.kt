package myplayground.example.dicodingstory.database

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.request.LoginRequest
import myplayground.example.dicodingstory.network.request.RegisterRequest
import myplayground.example.dicodingstory.network.response.FileUploadResponse
import myplayground.example.dicodingstory.network.response.ListStoryResponse
import myplayground.example.dicodingstory.network.response.LoginResponse
import myplayground.example.dicodingstory.network.response.RegisterResponse
import myplayground.example.dicodingstory.network.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.test.runTest
import retrofit2.Response

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private var mockApi: DicodingStoryApi = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, Story>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
        Assert.assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

}

class FakeApiService : DicodingStoryApi {
    override suspend fun fetchStories(
        page: Int?,
        size: Int?,
        location: Int?
    ): Response<ListStoryResponse> {
        if (page == null || size == null) {
            error("page and size must be supplied")
        }

        val items: MutableList<StoryResponse> = listOf<StoryResponse>().toMutableList()
        for (i in 0..100) {
            val storyResponse = StoryResponse(
                i.toString(),
                "name-$i",
                "desc-$i",
                "https://pic.com",
                "",
                0F,
                0F,
            )
            items.add(storyResponse)
        }
        return Response.success(
            ListStoryResponse(
                false,
                "OK",
                items.subList((page - 1) * size, (page - 1) * size + size),
            )
        )
    }

    override suspend fun register(body: RegisterRequest): Response<RegisterResponse> {
        error("no need to implement")
    }

    override suspend fun login(body: LoginRequest): Response<LoginResponse> {
        error("no need to implement")
    }

    override suspend fun addStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Float?,
        lon: Float?
    ): Response<FileUploadResponse> {
        error("no need to implement")
    }
}
