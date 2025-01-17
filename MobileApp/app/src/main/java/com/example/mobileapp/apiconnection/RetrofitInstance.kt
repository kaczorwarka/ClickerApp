package com.example.mobileapp.apiconnection

import com.example.mobileapp.apiconnection.auth.AuthCRUD
import com.example.mobileapp.apiconnection.game.GameCRUD
import com.example.mobileapp.apiconnection.user.UserCRUD
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val authCrud: AuthCRUD by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthCRUD::class.java)
    }

    val userCrud: UserCRUD by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/user/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserCRUD::class.java)
    }

    val gameCrud: GameCRUD by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/games/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameCRUD::class.java)
    }
}