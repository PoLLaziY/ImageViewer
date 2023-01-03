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
import com.example.imageviewer.view.values.Default
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeScreenViewModel(val repository: ImageRepository? = null) :
    ViewModel(), OpenedImageOnClickListener {

    private val imagePager =
        Pager(PagingConfig(pageSize = 20)) {
            repository?.newImagesSourceFactory()
                ?: CatImagePagingSource.listSource(Default.PREVIEW_CAT_IMAGES)
        }

    val images: StateFlow<PagingData<CatImage>> =
        imagePager.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    override fun update(catImage: CatImage) {
        repository?.update(catImage)
    }
}