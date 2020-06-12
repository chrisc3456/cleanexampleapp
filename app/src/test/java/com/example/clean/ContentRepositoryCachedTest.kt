package com.example.clean

import android.content.res.Resources
import com.example.clean.data.Result
import com.example.clean.data.localdb.ItemEntity
import com.example.clean.data.repository.ContentRepositoryCached
import com.example.clean.data.sources.ItemLocalDataSource
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class ContentRepositoryCachedTest {

    private lateinit var repository: ContentRepositoryCached
    @Mock private lateinit var localSource: ItemLocalDataSource

    // Set the main coroutines dispatcher for unit testing - Dispatchers.Main will refer to this in tests
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setupDependencies() {
        localSource = Mockito.mock(ItemLocalDataSource::class.java)
        repository = ContentRepositoryCached(localSource, Dispatchers.Main)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getContent_idProvided_localDataSourceQueriedWithId() = mainCoroutineRule.runBlockingTest {
        repository.getContent("test")
        verify(localSource).getItem("test")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getContent_idProvidedWithMatchingData_successfulResultWithData() = mainCoroutineRule.runBlockingTest {
        val item = ItemEntity("id", "date", "title", "excerpt", "content")
        `when`(localSource.getItem("test"))
            .thenReturn(item)

        val result = repository.getContent("test")

        assertTrue(result is Result.Success)
        (result as Result.Success).let {
            assertEquals("id", it.data.id)
            assertEquals("date", it.data.date)
            assertEquals("title", it.data.title)
            assertEquals("content", it.data.content)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getContent_idProvidedWithNoMatch_errorResultWithException() = mainCoroutineRule.runBlockingTest {
        val item = ItemEntity("", "", "", "", "")
        `when`(localSource.getItem("test"))
            .thenReturn(item)

        val result = repository.getContent("")

        assertTrue(result is Result.Error)
        assertEquals(Resources.getSystem().getString(R.string.errorNoData), (result as Result.Error).exception.message)
    }
}