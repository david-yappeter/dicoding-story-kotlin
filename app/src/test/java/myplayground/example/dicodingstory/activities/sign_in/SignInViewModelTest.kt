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
        // clean up after the tests
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

        Mockito.`when`(networkApi.login(LoginRequest(email, password)))
            .thenReturn(dummyLoginResponse)

        val isSuccessObserver = Mockito.mock(Observer::class.java) as Observer<Boolean>
        viewModel.isSuccess.observeForever(isSuccessObserver)

        viewModel.loginUser(email, password)

        kotlinx.coroutines.delay(1000)

        // verify isSuccess is set to true
        Mockito.verify(isSuccessObserver).onChanged(true)

        // verify login method is called with expected parameter
        Mockito.verify(networkApi).login(LoginRequest(email, password))

        // verify localStorage saveUserData
        Mockito.verify(localStorageManager).saveUserData(Mockito.any())

        // verify error message is not set
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `when network error should fail`() = runTest(mainDispatchers) {
        val email = "email@gmail.com"
        val password = "12345678"

        Mockito.`when`(networkApi.login(LoginRequest(email, password)))
            .thenAnswer {
                throw IOException("Network error")
            }

        val errorMessageObserver = Mockito.mock(Observer::class.java) as Observer<String>
        viewModel.errorMessage.observeForever(errorMessageObserver)

        val isSuccessObserver = Mockito.mock(Observer::class.java) as Observer<Boolean>
        viewModel.isSuccess.observeForever(isSuccessObserver)

        viewModel.loginUser(email, password)

        kotlinx.coroutines.delay(1000)

        // verify is success is set to false
        Mockito.verify(isSuccessObserver).onChanged(false)

        // verify login function is called with expected parameter
        Mockito.verify(networkApi).login(LoginRequest(email, password))

        // verify errorMessage is set
        assertNotNull(viewModel.errorMessage.value)

        // verify errorMessage content
        Mockito.verify(errorMessageObserver).onChanged("Network error")
    }
}