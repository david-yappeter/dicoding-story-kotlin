package myplayground.example.dicodingstory.activities.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StoryDetailViewModel : ViewModel() {
    val errorMessage = MutableLiveData<String>()
}