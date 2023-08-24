package myplayground.example.dicodingstory.local_storage.model

import com.google.gson.annotations.SerializedName
import myplayground.example.dicodingstory.network.response.LoginResultResponse

data class UserData(
    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("token")
    val token: String? = null
) {
    companion object {
        fun fromLoginResultResponse(resp: LoginResultResponse) = UserData(
            resp.userId,
            resp.name,
            resp.token,
        )
    }
}
