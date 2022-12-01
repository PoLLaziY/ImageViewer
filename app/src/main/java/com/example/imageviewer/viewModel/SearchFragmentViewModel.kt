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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SearchFragmentViewModel(private val webService: WebService) : ViewModel() {

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
                val tags = getTagsFromQuery(query)
                field = CatImagePagingSource(webService, getBreedFromTags(tags)?.id, getCategoryFromTags(tags)?.id)
                return field
            }
            return field
        }

    val images: StateFlow<PagingData<CatImage>> = Pager(PagingConfig(pageSize = 20)) {
        imagePagingSource?: CatImagePagingSource(webService)
    }.flow.cachedIn(viewModelScope).stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun search(query: String?) {
        Log.i("SearchFragment", "ViewModel get = $query")
        this.query = query
    }

    private fun getBreedFromTags(tags: List<String>?): Breed? {
        if (tags == null) return null
        if (breeds.value == null) return null
        val result: Breed? = breeds.value!!.firstOrNull() { breed ->
            tags.any { tag ->
                if (breed.name == null) return@any false
                breed.name!!.contains(tag, true)
                        || tag.contains(breed.name!!, true)
            }
        } ?: breeds.value!!.firstOrNull() { breed ->
            tags.any { tag ->
                if (breed.altNames == null) return@any false
                breed.altNames!!.contains(tag, true)
                        || tag.contains(breed.altNames!!, true)
            }
        } ?: breeds.value!!.firstOrNull() { breed ->
            tags.any { tag ->
                if (breed.temperament == null) return@any false
                breed.temperament!!.contains(tag, true)
                        || tag.contains(breed.temperament!!, true)
            }
        }
        return result
    }

    private fun getCategoryFromTags(tags: List<String>?): Category? {
        if (tags == null) return null
        if (categories.value == null) return null
        return categories.value!!.firstOrNull() { category ->
            tags.any { tag ->
                (category.name?.contains(tag, true) ?: false)
                        || if (category.name == null) false else tag.contains(category.name!!, true)
            }
        }
    }

    private fun getTagsFromQuery(query: String?): List<String>? {
        if (query.isNullOrEmpty()) return null
        var substringsStartIndex = 0
        val tags = mutableListOf<String>()
        query.toCharArray().forEachIndexed { index, c ->
            if (c == ' ' || c == ',' || c == '.' || c == '/') {
                if (substringsStartIndex < index) {
                    tags.add(query.substring(substringsStartIndex, index).lowercase())
                }
                substringsStartIndex = index + 1
            }
            if (index == query.lastIndex) {
                tags.add(query.substring(substringsStartIndex, index + 1).lowercase())
            }
        }
        return tags
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