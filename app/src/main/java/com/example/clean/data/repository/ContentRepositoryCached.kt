package com.example.clean.data.repository

import android.content.res.Resources
import com.example.clean.R
import com.example.clean.data.Result
import com.example.clean.data.sources.ItemLocalDataSource
import com.example.clean.viewobjects.Content
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContentRepositoryCached @Inject constructor(
    private val localDataSource: ItemLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO): ContentRepository {

    override suspend fun getContent(id: String): Result<Content> {

        // Retrieve the full item content details from the local data source
        return withContext(dispatcher) {
            val item = localDataSource.getItem(id)
            item?.let {
                return@withContext Result.Success(
                    Content(it.id, it.date, it.title, it.content)
                )
            }

            // Return an error if an item cannot be found with the specified id
            return@withContext Result.Error(Exception(Resources.getSystem().getString(R.string.errorNoData)))
        }
    }
}