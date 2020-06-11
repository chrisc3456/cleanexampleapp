package com.example.clean.data.sources

import com.example.clean.api.AzureResponseItem
import com.example.clean.data.localdb.ItemEntity

interface ItemLocalDataSource {
    fun getItems(): List<ItemEntity>
    fun getItem(id: String): ItemEntity?
    fun updateWithRemoteItems(items: List<AzureResponseItem>)
}