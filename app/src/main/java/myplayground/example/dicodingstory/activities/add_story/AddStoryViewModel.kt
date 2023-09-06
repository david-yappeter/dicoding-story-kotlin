package myplayground.example.dicodingstory.activities.add_story

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.Uri
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

class AddStoryViewModel(
    private val networkApi: DicodingStoryApi, private val locationManager: LocationManager
) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val isSuccess = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val imageFile = MutableLiveData<File?>()
    val galleryUri = MutableLiveData<Uri?>()
    val cameraBitmap = MutableLiveData<Bitmap?>()
    val isIncludeLocation = MutableLiveData(false)

    @SuppressLint("MissingPermission")
    fun addStory(description: String) {
        if (imageFile.value != null) {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    isLoading.value = true
                }
            }
            try {
                if (isIncludeLocation.value == true) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,
                        5f,
                    ) { location ->
                        viewModelScope.launch {
                            submitStory(
                                description,
                                location.latitude.toFloat(),
                                location.longitude.toFloat()
                            )
                        }
                    }
                } else {
                    viewModelScope.launch {
                        submitStory(description)
                    }
                }
                return

            } finally {
                isLoading.postValue(false)
            }
        }
    }

    private suspend fun submitStory(description: String, lat: Float? = null, lon: Float? = null) {
        val file = FileUtils.reduceFileImage(imageFile.value as File)
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile,
        )

        val response = networkApi.addStory(imageMultipart, descriptionRequestBody, lat, lon)
        if (response.isSuccessful) {
            isSuccess.postValue(true)
            isLoading.postValue(false)
        } else {
            onError("Error: ${response.message()}")
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        isLoading.postValue(false)
    }

    //    override fun onLocationChanged(location: Location) {
    //    }
}