package myplayground.example.dicodingstory.activities.sign_in

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import myplayground.example.dicodingstory.local_storage.LocalStorageManager
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.request.LoginRequest
import myplayground.example.dicodingstory.network.response.LoginResponse
import myplayground.example.dicodingstory.network.response.LoginResultResponse
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
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SignInViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkApi: DicodingStoryApi

    @Mock
    private lateinit var localStorageManager: LocalStorageManager

    private lateinit var viewModel: SignInViewModel
    private lateinit var mainDispatchers: TestDispatcher

    @Before
    fun setUp() {
        mainDispatchers = UnconfinedTestDispatcher()
        Dispatchers.setMain(mainDispatchers)
        viewModel = SignInViewModel(networkApi, localStorageManager)

    }

    @After
    fun tearDown() {
        // Clean up after the tests
        Dispatchers.resetMain()
    }

    @Test
    fun `when login should not null and success`() = runTest(mainDispatchers) {
        val dummyLoginResponse = Response.success(
            LoginResponse(
                false,
                "Success",
                LoginResultResponse("as", "name", "token")
            )
        )
        val email = "email@gmail.com"
        val password = "12345678"

        // Mock successful login response
        Mockito.`when`(networkApi.login(LoginRequest(email, password)))
            .thenReturn(dummyLoginResponse)

        // Observer for isSuccess LiveData
        val isSuccessObserver = Mockito.mock(Observer::class.java) as Observer<Boolean>
        viewModel.isSuccess.observeForever(isSuccessObserver)

        // Call the loginUser method
        viewModel.loginUser(email, password)

        kotlinx.coroutines.delay(1000)

        // Verify that isSuccess LiveData is set to true
        Mockito.verify(isSuccessObserver).onChanged(true)

        // Verify that the login method was called with the expected parameters
        Mockito.verify(networkApi).login(LoginRequest(email, password))

        // Verify that the user data is saved to local storage
        Mockito.verify(localStorageManager).saveUserData(Mockito.any())

        // Verify that the error message LiveData is not set
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `when network error should fail`() = runTest(mainDispatchers) {
        val email = "email@gmail.com"
        val password = "12345678"

        // Mock successful login response
        Mockito.`when`(networkApi.login(LoginRequest(email, password)))
            .thenAnswer {
                throw IOException("Network error")
            }

        // Observer for errorMessage LiveData
        val errorMessageObserver = Mockito.mock(Observer::class.java) as Observer<String>
        viewModel.errorMessage.observeForever(errorMessageObserver)

        // Observer for isSuccess LiveData
        val isSuccessObserver = Mockito.mock(Observer::class.java) as Observer<Boolean>
        viewModel.isSuccess.observeForever(isSuccessObserver)

        // Call the loginUser method
        viewModel.loginUser(email, password)

        kotlinx.coroutines.delay(1000)

        // Verify that isSuccess LiveData is set to false
        Mockito.verify(isSuccessObserver).onChanged(false)

        // Verify that the login method was called with the expected parameters
        Mockito.verify(networkApi).login(LoginRequest(email, password))

        // Verify that the error message LiveData is set
        assertNotNull(viewModel.errorMessage.value)

        Mockito.verify(errorMessageObserver).onChanged("Network error")
    }
}