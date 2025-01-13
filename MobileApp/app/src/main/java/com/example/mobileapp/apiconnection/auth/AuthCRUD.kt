package com.example.mobileapp.apiconnection.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthCRUD {

    @POST("login")
    suspend fun getToken(@Body authRequest: AuthenticationRequest): Response<AuthenticationResponse>
}