package myplayground.example.dicodingstory.model

import myplayground.example.dicodingstory.network.response.StoryResponse

data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val created_at: String?,
    val lat: Float?,
    val lon: Float?,
) {
    companion object {
        fun fromStoryResponse(resp: StoryResponse) = Story(
            resp.id,
            resp.name,
            resp.description,
            resp.photoUrl,
            resp.createdAt,
            resp.lat,
            resp.lon,
        )
    }
}