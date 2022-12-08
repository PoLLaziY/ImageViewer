package com.example.imageviewer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.source.CatImagePagingSource
import com.example.imageviewer.source.ImageRepository
import com.example.imageviewer.source.ImageStateUpdater
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FavoriteFragmentViewModel(private val repository: ImageRepository) : ViewModel(),
    ImageStateUpdater by repository {

    var needFavorite: Boolean = false
        set(value) {
            if (field == value) return
            else {
                field = value
                imageSource.invalidate()
            }
        }
    var needLiked: Boolean = false
        set(value) {
            if (field == value) return
            else {
                field = value
                imageSource.invalidate()
            }
        }
    var needWatched: Boolean = false
        set(value) {
            if (field == value) return
            else {
                field = value
                imageSource.invalidate()
            }
        }

    private var imageSource = CatImagePagingSource()
        get() {
            if (field.invalid) {
                field = repository.favoriteImagesFactory(needFavorite, needLiked, needWatched)
            }
            return field
        }

    val images: StateFlow<PagingData<CatImage>> =
        Pager(PagingConfig(pageSize = 20)) {
            imageSource
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
}