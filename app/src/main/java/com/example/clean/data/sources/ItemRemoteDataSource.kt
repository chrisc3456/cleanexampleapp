package com.example.clean.data.sources

import com.example.clean.api.AzureResponseItem
import com.example.clean.data.Result

interface ItemRemoteDataSource {
    fun getItems(): Result<List<AzureResponseItem>>
}