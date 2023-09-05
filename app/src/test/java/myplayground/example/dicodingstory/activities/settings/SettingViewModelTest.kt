package myplayground.example.dicodingstory.activities.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import myplayground.example.dicodingstory.local_storage.LocalStorageManager
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
class SettingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var localStorageManager: LocalStorageManager

    private lateinit var viewModel: SettingViewModel
    private lateinit var mainDispatchers: TestDispatcher

    @Before
    fun setUp() {
        mainDispatchers = UnconfinedTestDispatcher()
        Dispatchers.setMain(mainDispatchers)
        viewModel = SettingViewModel(localStorageManager)
    }

    @After
    fun tearDown() {
        // clean up after the tests
        Dispatchers.resetMain()
    }

    @Test
    fun `when login should not null and success`() = runTest(mainDispatchers) {
        viewModel.logout()

        delay(1000)

        // verify localStorage saveUserData
        Mockito.verify(localStorageManager).saveUserData(null)
    }
}