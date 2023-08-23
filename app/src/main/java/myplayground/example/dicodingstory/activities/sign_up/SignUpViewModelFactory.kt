package myplayground.example.dicodingstory.activities.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.dicodingstory.network.DicodingStoryApi
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory(private val networkApi: DicodingStoryApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(networkApi) as T
        }
        throw IllegalArgumentException("Unknown view class model")
    }
}