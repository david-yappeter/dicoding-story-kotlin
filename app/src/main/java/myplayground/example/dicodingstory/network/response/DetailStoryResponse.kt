package myplayground.example.dicodingstory.network.response

import java.io.Serializable

data class DetailStoryResponse(
    val error: Boolean,
    val message: String,
    val story: StoryResponse?
) : Serializable