package com.example.allen.cathubclient

import retrofit2.Call
import retrofit2.http.GET

interface CatHubApi {
    @GET("/api/images.json?count=10&index=2")
    fun getImages() : Call<CatImage>
}
