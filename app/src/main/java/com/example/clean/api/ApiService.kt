package com.example.clean.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("item")
    fun getItems(): Call<List<ApiResponseItem>>

    @GET("item/{id}")
    fun getItem(
        @Path("id") id: Int
    ): Call<ApiResponseItem>
}