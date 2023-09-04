package myplayground.example.dicodingstory.activities.sign_up

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.request.RegisterRequest
import myplayground.example.dicodingstory.network.response.RegisterResponse
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
class SignUpViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkApi: DicodingStoryApi

    private lateinit var viewModel: SignUpViewModel
    private lateinit var mainDispatchers: TestDispatcher

    @Before
    fun setUp() {
        mainDispatchers = UnconfinedTestDispatcher()
        Dispatchers.setMain(mainDispatchers)
        viewModel = SignUpViewModel(networkApi)

    }

    @After
    fun tearDown() {
        // clean up after the tests
        Dispatchers.resetMain()
    }

    @Test
    fun `when login should not null and success`() = runTest(mainDispatchers) {
        val dummyLoginResponse = Response.success(
            RegisterResponse(
                false,
                "Success",
            )
        )
        val name = "John Doe"
        val email = "email@gmail.com"
        val password = "12345678"

        // mock sign up response
        Mockito.`when`(networkApi.register(RegisterRequest(name, email, password)))
            .thenReturn(dummyLoginResponse)

        val isSuccessObserver = Mockito.mock(Observer::class.java) as Observer<Boolean>
        viewModel.isSuccess.observeForever(isSuccessObserver)

        viewModel.registerUser(name, email, password)

        kotlinx.coroutines.delay(1000)

        // verify is success is set to true
        Mockito.verify(isSuccessObserver).onChanged(true)

        // verify the function is called with expected parameter
        Mockito.verify(networkApi).register(RegisterRequest(name, email, password))

        // Verify that the error message LiveData is not set
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `when network error should fail`() = runTest(mainDispatchers) {
        val name = "John Doe"
        val email = "email@gmail.com"
        val password = "12345678"

        Mockito.`when`(networkApi.register(RegisterRequest(name, email, password)))
            .thenAnswer {
                throw IOException("Network error")
            }

        val errorMessageObserver = Mockito.mock(Observer::class.java) as Observer<String>
        viewModel.errorMessage.observeForever(errorMessageObserver)

        val isSuccessObserver = Mockito.mock(Observer::class.java) as Observer<Boolean>
        viewModel.isSuccess.observeForever(isSuccessObserver)

        // Call the loginUser method
        viewModel.registerUser(name, email, password)

        kotlinx.coroutines.delay(1000)

        // Verify that isSuccess LiveData is set to false
        Mockito.verify(isSuccessObserver).onChanged(false)

        // Verify that the login method was called with the expected parameters
        Mockito.verify(networkApi).register(RegisterRequest(name, email, password))

        // Verify that the error message LiveData is set
        assertNotNull(viewModel.errorMessage.value)

        Mockito.verify(errorMessageObserver).onChanged("Network error")
    }
}