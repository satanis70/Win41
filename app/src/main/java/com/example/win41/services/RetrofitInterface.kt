package com.example.win41.services

import com.example.win41.model.BodyModel
import com.example.win41.model.PlayersModel
import com.example.win41.model.PostModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitInterface {
    @POST("splash.php")
    suspend fun postQuery(@Body postModel: PostModel): Response<BodyModel>
    @GET("topTennisPlayers.json")
    fun getPlayers(): Call<PlayersModel>
}