package com.example.clean

import com.example.clean.api.AzureResponseItem
import com.example.clean.data.Result
import com.example.clean.data.localdb.ItemEntity
import com.example.clean.data.model.Summary
import com.example.clean.data.repository.SummaryRepositoryRemoteCached
import com.example.clean.data.sources.ItemLocalDataSource
import com.example.clean.data.sources.ItemRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*

class SummaryRepositoryRemoteCachedTest {

    private lateinit var repository: SummaryRepositoryRemoteCached
    @Mock private lateinit var remoteSource: ItemRemoteDataSource
    @Mock private lateinit var localSource: ItemLocalDataSource

    // Set the main coroutines dispatcher for unit testing - Dispatchers.Main will refer to this in tests
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setupDependencies() {
        remoteSource = mock(ItemRemoteDataSource::class.java)
        localSource = mock(ItemLocalDataSource::class.java)
        repository = SummaryRepositoryRemoteCached(remoteSource, localSource, Dispatchers.Main)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getSummaries_forceRefreshFalse_noRemoteSourceInteraction() = mainCoroutineRule.runBlockingTest {
        val result = repository.getSummaries(false)
        verifyNoInteractions(remoteSource)
        assertTrue(result is Result.Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getSummaries_forceRefreshFalse_localSourceQueried() = mainCoroutineRule.runBlockingTest {
        val result = repository.getSummaries(false)
        verify(localSource).getItems()
        assertTrue(result is Result.Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getSummaries_forceRefreshTrue_remoteSourceQueried() = mainCoroutineRule.runBlockingTest {
        val result = repository.getSummaries(true)
        verify(remoteSource).getItems()
        assertTrue(result is Result.Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getSummaries_forceRefreshFalse_successResultPopulated() = mainCoroutineRule.runBlockingTest {
        `when`(localSource.getItems())
            .thenReturn(listOf(
                ItemEntity("TestId", "TestDate", "TestTitle", "TestExcerpt", "TestContent")
            ))

        val result = repository.getSummaries(false)
        val firstSummary = (result as Result.Success<List<Summary>>).data.first()

        assertEquals("TestId", firstSummary.id)
        assertEquals("TestDate", firstSummary.date)
        assertEquals("TestTitle", firstSummary.title)
        assertEquals("TestExcerpt", firstSummary.excerpt)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getSummaries_forceRefreshFalse_resultsSortedByDate() = mainCoroutineRule.runBlockingTest {
        `when`(localSource.getItems())
            .thenReturn(listOf(
                ItemEntity("", "B", "", "", ""),
                ItemEntity("", "D", "", "", ""),
                ItemEntity("", "A", "", "", ""),
                ItemEntity("", "C", "", "", "")
            ))

        val summaries = (repository.getSummaries(false) as Result.Success<List<Summary>>).data

        assertTrue(summaries[0].date == "A")
        assertTrue(summaries[1].date == "B")
        assertTrue(summaries[2].date == "C")
        assertTrue(summaries[3].date == "D")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getSummaries_remoteError_resultError() = mainCoroutineRule.runBlockingTest {
        val ex = Exception("test")
        `when`(remoteSource.getItems())
            .thenReturn(Result.Error(ex))

        val result = repository.getSummaries(true)

        assertTrue(result is Result.Error)
        assertEquals(ex, (result as Result.Error).exception)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getSummaries_remoteErrorWithPreviousResults_resultSuccess() = mainCoroutineRule.runBlockingTest {
        `when`(localSource.getItems())
            .thenReturn(listOf(
                ItemEntity("", "", "", "", "")
            ))
        repository.getSummaries(false)

        val ex = Exception("test")
        `when`(remoteSource.getItems())
            .thenReturn(Result.Error(ex))

        val result = repository.getSummaries(true)

        assertTrue(result is Result.Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getSummaries_remoteCalledWithSuccess_localDataSourceUpdated() = mainCoroutineRule.runBlockingTest {
        val remoteResponseList = listOf(
            AzureResponseItem("", "", "", "", "")
        )
        `when`(remoteSource.getItems())
            .thenReturn(Result.Success(remoteResponseList)
            )

        repository.getSummaries(true)
        verify(localSource).updateWithRemoteItems(remoteResponseList)
    }
}