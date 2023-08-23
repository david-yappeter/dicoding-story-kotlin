package myplayground.example.dicodingstory.activities.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.dicodingstory.network.DicodingStoryApi
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class SignInViewModelFactory(private val networkApi: DicodingStoryApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(networkApi) as T
        }
        throw IllegalArgumentException("Unknown view class model")
    }
}