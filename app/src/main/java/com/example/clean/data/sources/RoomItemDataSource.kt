package com.example.clean.data.sources

import com.example.clean.api.AzureResponseItem
import com.example.clean.data.localdb.ItemDatabase
import com.example.clean.data.localdb.ItemEntity
import javax.inject.Inject

class RoomItemDataSource @Inject constructor(private val database: ItemDatabase):
    ItemLocalDataSource {

    override fun getItems(): List<ItemEntity> {
        return database.itemDao().getItems()
    }

    override fun getItem(id: String): ItemEntity? {
        return database.itemDao().getItem(id)
    }

    override fun updateWithRemoteItems(items: List<AzureResponseItem>) {
        database.itemDao().addItems(
            items.map { item -> ItemEntity(id = item.id, date = item.date, title = item.title, excerpt = item.excerpt, content = item.content) }
        )
    }
}