package com.example.clean.data.repository

import com.example.clean.data.model.Summary
import com.example.clean.data.Result

interface SummaryRepository {
    suspend fun getSummaries(forceRefresh: Boolean): Result<List<Summary>>
}