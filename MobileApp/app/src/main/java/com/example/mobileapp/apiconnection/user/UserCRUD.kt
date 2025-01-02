package com.example.mobileapp.apiconnection.user

import com.example.mobileapp.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserCRUD {

    @GET("{email}")
    suspend fun getUser(@Path("email") email: String, @Header("Authorization") token: String):Response<User>
}