package com.example.clean.data.repository

import com.example.clean.data.Result
import com.example.clean.viewobjects.Content

interface ContentRepository {
    suspend fun getContent(id: String): Result<Content>
}