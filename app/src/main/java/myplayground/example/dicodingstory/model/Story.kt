package myplayground.example.dicodingstory.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import myplayground.example.dicodingstory.network.response.StoryResponse

@Entity(tableName = "story")
@Parcelize
data class Story(
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("created_at")
    val createdAt: String?,

    @field:SerializedName("lat")
    val lat: Float?,

    @field:SerializedName("lon")
    val lon: Float?,
) : Parcelable {
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