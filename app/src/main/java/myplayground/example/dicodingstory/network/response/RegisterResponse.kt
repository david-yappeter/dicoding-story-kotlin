package myplayground.example.dicodingstory.network.response

import java.io.Serializable

data class RegisterResponse(
    val error: Boolean,
    val message: String,
) : Serializable