package com.example.imageviewer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.CatImagePagingSource
import com.example.imageviewer.web.WebService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ImagePagerViewModel(private val webService: WebService): ViewModel() {

    val images: StateFlow<PagingData<CatImage>> = Pager(PagingConfig(pageSize = 20)) {
        CatImagePagingSource(webService, null)
    }.flow.stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

}