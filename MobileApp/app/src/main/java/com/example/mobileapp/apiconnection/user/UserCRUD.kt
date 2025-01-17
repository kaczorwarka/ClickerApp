package com.example.mobileapp.apiconnection.user
import com.example.mobileapp.model.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserCRUD {

    @GET("{email}")
    suspend fun getUser(
        @Path("email") email: String,
        @Header("Authorization") token: String
    ):Response<User>

    @PUT("lives/{email}")
    suspend fun updateLives(
        @Path("email") email: String,
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ):Response<User>
}