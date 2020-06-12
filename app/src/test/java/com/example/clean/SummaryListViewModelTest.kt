package com.example.clean

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.clean.data.Result
import com.example.clean.viewobjects.Summary
import com.example.clean.data.repository.SummaryRepository
import com.example.clean.viewmodels.SummaryListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*

class SummaryListViewModelTest {

    private lateinit var viewModel: SummaryListViewModel
    @Mock private lateinit var repository: SummaryRepository

    // Set the main coroutines dispatcher for unit testing - Dispatchers.Main will refer to this in tests
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Swap background executor for a synchronous one - view model uses viewModelScope to execute in a background job
    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @Before
    fun setupDependencies() {
        repository = mock(SummaryRepository::class.java)
        viewModel = SummaryListViewModel(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadItems_forceRefreshFalse_repositoryCalledNoRefresh() = mainCoroutineRule.runBlockingTest {
        viewModel.loadItems(false)
        verify(repository).getSummaries(false)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadItems_initOnly_repositoryCalledWithRefresh() = mainCoroutineRule.runBlockingTest {
        verify(repository).getSummaries(true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadItems_forceRefreshTrue_repositoryCalledWithRefresh() = mainCoroutineRule.runBlockingTest {
        viewModel.loadItems(true)
        verify(repository, times(2)).getSummaries(true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadItems_repositorySuccess_itemsSetNoErrorOrLoadingState() = mainCoroutineRule.runBlockingTest {
        val items = listOf(
            Summary("", "", "", "")
        )
        `when`(repository.getSummaries(true))
            .thenReturn(Result.Success(items))

        viewModel.loadItems(true)

        assertFalse(viewModel.isLoadingError.value!!)
        assertEquals(items, viewModel.items.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadItems_repositoryError_loadingError() = mainCoroutineRule.runBlockingTest {
        `when`(repository.getSummaries(true))
            .thenReturn(Result.Error(Exception()))

        viewModel.loadItems(true)

        assertTrue(viewModel.isLoadingError.value!!)
        assertTrue(viewModel.items.value.isNullOrEmpty())
    }
}