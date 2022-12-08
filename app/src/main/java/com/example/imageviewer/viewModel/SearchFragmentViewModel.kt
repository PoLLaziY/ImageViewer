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
import java.util.*

class SearchFragmentViewModel(
    private val repository: ImageRepository
) : ViewModel(), ImageStateUpdater by repository {

    private var imageSource: CatImagePagingSource = CatImagePagingSource()
        get() {
            if (field.invalid) {
                field = repository.foundImagesSourceFactory(query)
            }
            return field
        }

    val images: StateFlow<PagingData<CatImage>> =
        Pager(PagingConfig(pageSize = 20)) {
            imageSource
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    var query: String? = null
        set(value) {
            if (field == value) return
            field = value
            imageSource.invalidate()
        }
}