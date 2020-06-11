package com.example.clean.data.repository

import android.content.res.Resources
import com.example.clean.R
import com.example.clean.api.AzureResponseItem
import com.example.clean.data.Result
import com.example.clean.data.localdb.ItemEntity
import com.example.clean.data.sources.ItemLocalDataSource
import com.example.clean.data.sources.ItemRemoteDataSource
import com.example.clean.viewobjects.Summary
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SummaryRepositoryRemoteCached @Inject constructor(
    private val remoteDataSource: ItemRemoteDataSource,
    private val localDataSource: ItemLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO): SummaryRepository {

    private var cachedItems: List<ItemEntity>? = null

    /**
     * Retrieve a list of summary data from remote and/or local storage as required
     */
    override suspend fun getSummaries(forceRefresh: Boolean): Result<List<Summary>> {

        return withContext(dispatcher) {

            // Refresh the cache with data from the remote service if required
            if (forceRefresh) {
                val newItemsResult = getRefreshedItemsFromRemoteSource()

                // Return error details from the remote request if items are also not cached locally
                (newItemsResult as? Result.Error)?.let {
                    if (cachedItems.isNullOrEmpty()) {
                        return@withContext Result.Error(it.exception)
                    }
                }
            }

            cachedItems = localDataSource.getItems()

            // If we have items cached then we can return results successfully
            cachedItems?.let {
                return@withContext Result.Success(
                    cachedItems!!.map { Summary(id = it.id, date = it.date, title = it.title, excerpt = it.excerpt) }.sortedByDescending { it.date }
                )
            }

            // Catch-all for any further errors
            return@withContext Result.Error(Exception(Resources.getSystem().getString(R.string.errorNoData)))
        }
    }

    /**
     * Retrieves updated list of items from the remote data source and update the local cache with the results if successful
     */
    private fun getRefreshedItemsFromRemoteSource(): Result<List<AzureResponseItem>> {
        val newItemsResult = remoteDataSource.getItems()
        (newItemsResult as? Result.Success)?.let {
            localDataSource.updateWithRemoteItems(it.data)
        }

        return newItemsResult
    }
}