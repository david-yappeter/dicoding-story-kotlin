package myplayground.example.dicodingstory.network.request

import java.io.Serializable

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
) : Serializable