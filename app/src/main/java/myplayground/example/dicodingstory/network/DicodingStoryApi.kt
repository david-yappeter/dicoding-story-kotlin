package myplayground.example.dicodingstory.network

import myplayground.example.dicodingstory.network.request.LoginRequest
import myplayground.example.dicodingstory.network.request.RegisterRequest
import myplayground.example.dicodingstory.network.response.LoginResponse
import myplayground.example.dicodingstory.network.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DicodingStoryApi {
    companion object {
        const val BASE_URL = "https://story-api.dicoding.dev/v1/"
    }

    @POST("register")
    suspend fun register(
        @Body body: RegisterRequest,
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun login(
        @Body body: LoginRequest,
    ): Response<LoginResponse>
}