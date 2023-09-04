package myplayground.example.dicodingstory.activities.sign_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.request.RegisterRequest

class SignUpViewModel(private val networkApi: DicodingStoryApi) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val isSuccess = MutableLiveData<Boolean>()
    private val backgroundExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.localizedMessage?.let { onError(it) }
    }

    fun registerUser(
        name: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch(Dispatchers.IO + backgroundExceptionHandler) {
            withContext(Dispatchers.Main) {
                isLoading.value = true
            }

            try {
                val response = networkApi.register(
                    RegisterRequest(name, email, password)
                )
                if (response.isSuccessful) {
                    isSuccess.postValue(true)
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
        isSuccess.postValue(false)
    }
}