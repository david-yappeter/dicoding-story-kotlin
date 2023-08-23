package myplayground.example.dicodingstory.network.response

import java.io.Serializable

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResultResponse?,
) : Serializable

data class LoginResultResponse(
    val userId: String,
    val name: String,
    val token: String,
) : Serializable