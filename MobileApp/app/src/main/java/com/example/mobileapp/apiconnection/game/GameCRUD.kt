package com.example.mobileapp.apiconnection.game

import com.example.mobileapp.model.Game
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface GameCRUD {

    @POST("{email}")
    suspend fun saveGame(
        @Path("email") email: String,
        @Header("Authorization") token: String,
        @Body game: Game
    ): Response<Unit>
}