package com.example.clean

import com.example.clean.api.AzureFunctionService
import com.example.clean.api.AzureResponseItem
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.BufferedSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MockErrorAzureFunctionService: AzureFunctionService {
    override fun getItems(): Call<List<AzureResponseItem>> {
        return object: Call<List<AzureResponseItem>> {
            override fun enqueue(callback: Callback<List<AzureResponseItem>>) {}
            override fun isExecuted(): Boolean { return false }
            override fun isCanceled(): Boolean { return false }
            override fun clone(): Call<List<AzureResponseItem>> { return this }
            override fun cancel() {}
            override fun request(): Request { return Request.Builder().build() }
            override fun execute(): Response<List<AzureResponseItem>> {
                return Response.error(404, "".toResponseBody("application/json".toMediaTypeOrNull()))
            }
        }
    }

    override fun getItem(id: Int): Call<AzureResponseItem> {
        TODO("Not yet implemented")
    }
}