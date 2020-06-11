package com.example.clean.data.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItems(items: List<ItemEntity>)

    @Query("SELECT * FROM ItemEntity")
    fun getItems(): List<ItemEntity>

    @Query("SELECT * FROM ItemEntity WHERE id = :id")
    fun getItem(id: String): ItemEntity?

}