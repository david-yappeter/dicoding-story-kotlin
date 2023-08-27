package myplayground.example.dicodingstory.model

import android.os.Parcel
import android.os.Parcelable
import myplayground.example.dicodingstory.network.response.StoryResponse

data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String?,
    val lat: Float?,
    val lon: Float?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(photoUrl)
        parcel.writeString(createdAt)
        parcel.writeValue(lat)
        parcel.writeValue(lon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Story> {

        fun fromStoryResponse(resp: StoryResponse) = Story(
            resp.id,
            resp.name,
            resp.description,
            resp.photoUrl,
            resp.createdAt,
            resp.lat,
            resp.lon,
        )

        override fun createFromParcel(parcel: Parcel): Story {
            return Story(parcel)
        }

        override fun newArray(size: Int): Array<Story?> {
            return arrayOfNulls(size)
        }
    }
}