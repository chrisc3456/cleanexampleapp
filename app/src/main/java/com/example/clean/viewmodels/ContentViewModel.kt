package com.example.clean.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clean.data.Result
import com.example.clean.data.repository.ContentRepository
import com.example.clean.viewobjects.Content
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContentViewModel @Inject constructor(private val repository: ContentRepository): ViewModel() {

    val content = MutableLiveData<Content>()
    val isDataLoading = MutableLiveData<Boolean>()
    val isLoadingError = MutableLiveData<Boolean>()

    /**
     * Query the repository for an item with the required id
     */
    fun getContent(id: String) {
        isDataLoading.value = true

        viewModelScope.launch {
            val result = repository.getContent(id)

            if (result is Result.Success) {
                content.value = result.data
                isLoadingError.value = false
            } else {
                isLoadingError.value = true
            }

            isDataLoading.value = false
        }
    }
}