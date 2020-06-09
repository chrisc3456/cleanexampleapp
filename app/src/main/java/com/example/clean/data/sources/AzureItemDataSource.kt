package com.example.clean.data.sources

import android.content.res.Resources
import com.example.clean.R
import com.example.clean.api.AzureFunctionService
import com.example.clean.api.AzureResponseItem
import com.example.clean.data.Result
import javax.inject.Inject

class AzureItemDataSource @Inject constructor(private val azureFunctionService: AzureFunctionService):
    ItemRemoteDataSource {

    override fun getItems(): Result<List<AzureResponseItem>> {
        val serviceCall = azureFunctionService.getItems()

        return try {
            val response = serviceCall.execute()
            if (response.isSuccessful) {
                val items = response.body() ?: emptyList()
                Result.Success(items)
            } else {
                Result.Error(Exception(Resources.getSystem().getString(R.string.errorNoData)))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}