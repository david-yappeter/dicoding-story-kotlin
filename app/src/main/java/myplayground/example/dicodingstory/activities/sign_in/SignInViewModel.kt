package myplayground.example.dicodingstory.activities.sign_in

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.request.LoginRequest

class SignInViewModel(private val networkApi: DicodingStoryApi) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val backgroundExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled ${throwable.localizedMessage}")
    }

    fun loginUser(
        email: String,
        password: String,
    ) {
        viewModelScope.launch(Dispatchers.IO + backgroundExceptionHandler) {
            try {
                val response = networkApi.login(
                    LoginRequest(email, password)
                )
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                } else {
                    onError("Error: ${response.message()}")
                }
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        isLoading.postValue(false)
    }
}