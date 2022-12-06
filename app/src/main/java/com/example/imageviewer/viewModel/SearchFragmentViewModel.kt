package com.example.imageviewer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.source.ImageRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.*

class SearchFragmentViewModel(
    private val repository: ImageRepository
) : ViewModel() {

    var query: String?
        set(value) {
            repository.queryForSearch = value
        }
        get() = repository.queryForSearch

    val images: StateFlow<PagingData<CatImage>> =
        Pager(PagingConfig(pageSize = 20)) {
            repository.foundImagesSourceFactory!!
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    val favoriteButtonListener: (image: CatImage) -> Unit = {
        it.favorite = if (it.favorite > 0) 0 else Date().time
        repository.update(it)
    }

    val likeButtonListener: (image: CatImage) -> Unit = {
        it.liked = if (it.liked > 0) 0 else Date().time
        repository.update(it)
    }

}