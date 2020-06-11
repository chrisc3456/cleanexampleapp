package com.example.clean.data.repository

import com.example.clean.data.Result
import com.example.clean.viewobjects.Summary

interface SummaryRepository {
    suspend fun getSummaries(forceRefresh: Boolean): Result<List<Summary>>
}