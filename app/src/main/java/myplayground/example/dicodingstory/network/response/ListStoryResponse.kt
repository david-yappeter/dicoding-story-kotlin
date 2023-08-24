package myplayground.example.dicodingstory.network.response

import java.io.Serializable

data class ListStoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<StoryResponse>
) : Serializable