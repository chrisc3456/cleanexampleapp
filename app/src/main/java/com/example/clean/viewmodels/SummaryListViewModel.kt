package com.example.clean.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clean.data.Result
import com.example.clean.data.repository.SummaryRepository
import com.example.clean.viewobjects.Summary
import kotlinx.coroutines.launch
import javax.inject.Inject

class SummaryListViewModel @Inject constructor(private val repository: SummaryRepository): ViewModel() {

    val items = MutableLiveData<List<Summary>>().apply { value = emptyList() }
    val isDataLoading = MutableLiveData<Boolean>()
    val isLoadingError = MutableLiveData<Boolean>()

    init {
        loadItems(true)
    }

    /**
     * Query the repository for a list of items to display
     */
    fun loadItems(forceRefresh: Boolean) {
        isDataLoading.value = true

        viewModelScope.launch {
            val results = repository.getSummaries(forceRefresh)

            if (results is Result.Success) {
                items.value = results.data
                isLoadingError.value = false
            } else {
                isLoadingError.value = true
            }

            isDataLoading.value = false
        }
    }
}