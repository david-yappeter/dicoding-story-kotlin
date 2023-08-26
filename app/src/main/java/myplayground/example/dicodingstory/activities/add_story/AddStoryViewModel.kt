package myplayground.example.dicodingstory.activities.add_story

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.util.FileUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryViewModel(private val networkApi: DicodingStoryApi) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val isSuccess = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val imageFile = MutableLiveData<File?>()
    val galleryUri = MutableLiveData<Uri?>()
    val cameraBitmap = MutableLiveData<Bitmap?>()

    fun addStory(description: String) {
        if (imageFile.value != null) {
            val file = imageFile.value as File
            val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile,
            )

            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    isLoading.value = true
                }
                try {
                    val response = networkApi.addStory(imageMultipart, descriptionRequestBody)
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
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        isLoading.postValue(false)
    }
}