package com.example.imageviewer.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.source.CatImagePagingSource
import com.example.imageviewer.source.ImageRepository
import com.example.imageviewer.view.composeview.values.Default
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SearchScreenViewModel(val repository: ImageRepository? = null) : ViewModel(),
    OpenedImageOnClickListener {

    private var imageSource: PagingSource<Int, CatImage> = CatImagePagingSource()
        get() {
            if (field.invalid) {
                field =
                    repository?.foundImagesSourceFactory(query) ?: CatImagePagingSource.listSource(
                        Default.PREVIEW_CAT_IMAGES
                    )
            }
            return field
        }

    val images: StateFlow<PagingData<CatImage>> =
        Pager(PagingConfig(pageSize = 20)) {
            imageSource
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    private var _query: String? by mutableStateOf(null)

    override fun update(catImage: CatImage) {
        repository?.update(catImage)
    }

    var query: String?
        get() = _query
        set(value) {
            _query = value
            imageSource.invalidate()
        }
}