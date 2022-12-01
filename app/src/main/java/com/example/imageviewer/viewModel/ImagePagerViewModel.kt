package com.example.imageviewer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.source.CatImagePagingSource
import com.example.imageviewer.source.web.WebService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ImagePagerViewModel(
    private val webService: WebService,
    images: StateFlow<PagingData<CatImage>>? = null
): ViewModel() {

    val images: StateFlow<PagingData<CatImage>> = images ?: Pager(PagingConfig(pageSize = 20)) {
        CatImagePagingSource(webService, null)
    }.flow.cachedIn(viewModelScope).stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

}