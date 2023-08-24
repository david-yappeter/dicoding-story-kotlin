package myplayground.example.dicodingstory.network.response

import java.io.Serializable

data class StoryResponse(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Float?,
    val lon: Float?,
) : Serializable