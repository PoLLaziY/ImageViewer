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

open class ImagePagerViewModel(
    private val repository: ImageRepository
) : ViewModel() {

    val images: StateFlow<PagingData<CatImage>> =
        Pager(PagingConfig(pageSize = 20)) {
            repository.newImagesSourceFactory!!
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    val favoriteButtonListener: (image: CatImage) -> Unit = {
        it.isFavorite = !it.isFavorite
        repository.update(it)
    }

    val likeButtonListener: (image: CatImage) -> Unit = {
        it.liked = !it.liked
        repository.update(it)
    }
}