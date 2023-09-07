package myplayground.example.dicodingstory.activities.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.repository.StoryRepository

class HomeViewModel(
    storyRepository: StoryRepository,
) : ViewModel() {
    val pagerStories: LiveData<PagingData<Story>> =
        storyRepository.getStoryPager().cachedIn(viewModelScope)

}