package com.example.clean

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.clean.data.Result
import com.example.clean.data.repository.ContentRepository
import com.example.clean.viewmodels.ContentViewModel
import com.example.clean.viewobjects.Content
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*

class ContentViewModelTest {

    private lateinit var viewModel: ContentViewModel
    @Mock private lateinit var repository: ContentRepository

    // Set the main coroutines dispatcher for unit testing - Dispatchers.Main will refer to this in tests
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Swap background executor for a synchronous one - view model uses viewModelScope to execute in a background job
    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @Before
    fun setupDependencies() {
        repository = mock(ContentRepository::class.java)
        viewModel = ContentViewModel(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadItems_idProvided_repositoryCalledWithId() = mainCoroutineRule.runBlockingTest {
        viewModel.getContent("test")
        verify(repository).getContent("test")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadItems_repositorySuccess_contentSetNoErrorOrLoadingState() = mainCoroutineRule.runBlockingTest {
        val content = Content("", "", "", "")
        `when`(repository.getContent(""))
            .thenReturn(Result.Success(content))

        viewModel.getContent("")

        assertFalse(viewModel.isLoadingError.value!!)
        assertEquals(content, viewModel.content.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadItems_repositoryError_loadingError() = mainCoroutineRule.runBlockingTest {
        `when`(repository.getContent(""))
            .thenReturn(Result.Error(Exception()))

        viewModel.getContent("")

        assertTrue(viewModel.isLoadingError.value!!)
        assertNull(viewModel.content.value)
    }
}