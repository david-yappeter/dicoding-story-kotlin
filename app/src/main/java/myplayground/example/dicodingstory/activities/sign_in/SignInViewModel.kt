package myplayground.example.dicodingstory.activities.sign_in

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import myplayground.example.dicodingstory.local_storage.LocalStorageManager
import myplayground.example.dicodingstory.local_storage.model.UserData
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.request.LoginRequest

class SignInViewModel(
    private val networkApi: DicodingStoryApi,
    private val localStorageManager: LocalStorageManager
) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val isSuccess = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    private val backgroundExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled ${throwable.localizedMessage}")
    }

    fun loginUser(
        email: String,
        password: String,
    ) {
        viewModelScope.launch(Dispatchers.IO + backgroundExceptionHandler) {
            withContext(Dispatchers.Main) {
                isLoading.value = true
            }

            try {
                val response = networkApi.login(
                    LoginRequest(email, password)
                )
                if (response.isSuccessful) {
                    val body = response.body()

                    if (body?.loginResult != null) {
                        localStorageManager.saveUserData(UserData.fromLoginResultResponse(body.loginResult))
                    }
                    isLoading.postValue(false)
                    isSuccess.postValue(true)
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