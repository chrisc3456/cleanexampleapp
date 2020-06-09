package com.example.clean.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AzureFunctionService {

    @GET("item")
    fun getItems(): Call<List<AzureResponseItem>>

    @GET("item/{id}")
    fun getItem(
        @Path("id") id: Int
    ): Call<AzureResponseItem>
}