package com.example.imageviewer.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.imageviewer.domain.Breed
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.domain.Category
import com.example.imageviewer.source.CatImagePagingSource
import com.example.imageviewer.source.web.WebService
import com.example.imageviewer.source.web.WebServiceImpl
import com.example.imageviewer.utils.SearchAlgorithm
import com.example.imageviewer.utils.SearchAlgorithmImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SearchFragmentViewModel(
    private val webService: WebService
) : ViewModel() {

    private val breeds: StateFlow<List<Breed>?> =
        MutableStateFlow<List<Breed>?>(null).also { state ->
            viewModelScope.launch {
                state.emit(webService.getAllBreeds() ?: emptyList())
            }
        }

    private val categories: StateFlow<List<Category>?> =
        MutableStateFlow<List<Category>?>(null).also { state ->
            viewModelScope.launch {
                state.emit(webService.getAllCategories() ?: emptyList())
            }
        }

    private val searchAlgorithm: SearchAlgorithm = SearchAlgorithmImpl(breeds, categories).apply {
        webService.setSearchAlgorithm(this)
    }

    private var query: String? = null
        set(value) {
            viewModelScope.launch {
                if (value == field) return@launch
                if (breeds.value == null || categories.value == null) waitInitParams()
                Log.i("SearchFragment", "ViewModel query set $field")
                imagePagingSource?.invalidate()
                field = value
            }
        }

    private var imagePagingSource: CatImagePagingSource? = null
        get() {
            if (field == null || field?.invalid != false) {
                field = CatImagePagingSource(webService, query)
                return field
            }
            return field
        }

    val images: StateFlow<PagingData<CatImage>> = Pager(PagingConfig(pageSize = 20)) {
        imagePagingSource ?: CatImagePagingSource(webService)
    }.flow.cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun search(query: String?) {
        Log.i("SearchFragment", "ViewModel get = $query")
        this.query = query
    }

    private suspend fun waitInitParams(): Job =
        suspendCoroutine { coroutine ->

            Log.i("SearchCheck", "Wait start")
            val job = Job()
            CoroutineScope(coroutine.context + job).launch {
                var breedsInit = (breeds.value != null)
                var categoriesInit = (categories.value != null)

                this.launch {
                    categories.filter { it != null }.collect {
                        categoriesInit = true

                        Log.i("SearchCheck", "Stop wait")
                        if (breedsInit) coroutine.resume(job)
                    }
                }

                this.launch {
                    breeds.filter { it != null }.collect {
                        breedsInit = true

                        Log.i("SearchCheck", "Stop wait")
                        if (categoriesInit) coroutine.resume(job)
                    }
                }
            }
        }
}