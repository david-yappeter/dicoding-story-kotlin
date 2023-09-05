package myplayground.example.dicodingstory.activities.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import myplayground.example.dicodingstory.model.Story

class StoryDetailViewModel : ViewModel() {
    val errorMessage = MutableLiveData<String>()
}