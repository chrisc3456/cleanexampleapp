package com.example.clean.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity(
    @PrimaryKey
    val id: String,
    val date: String,
    val title: String,
    val excerpt: String,
    val content: String
)