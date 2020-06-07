package com.example.clean.data.repository

import android.content.res.Resources
import com.example.clean.R
import com.example.clean.api.ApiResponseItem
import com.example.clean.api.ApiService
import com.example.clean.data.Result
import com.example.clean.data.localdb.ItemDatabase
import com.example.clean.data.localdb.ItemEntity
import com.example.clean.data.model.Summary
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SummaryRepositoryImpl @Inject constructor(private val service: ApiService, private val database: ItemDatabase): SummaryRepository {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private var cachedItems: List<ItemEntity>? = null

    /**
     * Retrieve a list of summary data from remote and/or local storage as required
     */
    override suspend fun getSummaries(forceRefresh: Boolean): Result<List<Summary>> {

        return withContext(dispatcher) {

            // Refresh the cache with data from the remote service if required
            if (forceRefresh) {
                val newItemsResult = fetchItemsFromRemoteService()
                (newItemsResult as? Result.Success)?.let {
                    updateLocalDbWithRemoteData(it.data)
                }

                // Return error details from the remote request if applicable
                //(newItemsResult as? Result.Error)?.let {
                //    return@withContext Result.Error(it.exception)
                //}
            }

            cachedItems = fetchItemsFromLocalDb()

            cachedItems?.let {
                return@withContext Result.Success(
                    cachedItems!!.map { Summary(id = it.id, date = it.date, title = it.title, excerpt = it.excerpt) }.sortedBy { it.date }
                )
            }

            // Catch-all for any further errors
            return@withContext Result.Error(Exception(Resources.getSystem().getString(R.string.errorNoData)))
        }
    }

    /**
     * Retrieve a list of items from the remote service
     */
    private fun fetchItemsFromRemoteService(): Result<List<ApiResponseItem>> {
        val serviceCall = service.getItems()

        return try {
            val response = serviceCall.execute()
            if (response.isSuccessful) {
                val items = response.body() ?: emptyList()
                Result.Success(items)
            } else {
                Result.Error(Exception("Unable to retrieve data"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Retrieve the latest list of items from the local database
     */
    private fun fetchItemsFromLocalDb(): List<ItemEntity> {
        return database.itemDao().getItems()
    }

    /**
     * Convert the provided remote data into format for local database storage
     */
    private fun updateLocalDbWithRemoteData(results: List<ApiResponseItem>) {
        database.itemDao().addItems(
            results.map { item -> ItemEntity(id = item.id, date = item.date, title = item.title, excerpt = item.excerpt, content = item.content) }
        )
    }
}