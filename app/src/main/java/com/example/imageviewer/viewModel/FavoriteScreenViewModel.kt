package com.example.imageviewer.viewModel

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

class FavoriteScreenViewModel(val repository: ImageRepository? = null) : ViewModel(),
    OpenedImageOnClickListener {

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
    var needAlarmed: Boolean = false
        set(value) {
            if (field == value) return
            else {
                field = value
                imageSource.invalidate()
            }
        }

    private var imageSource: PagingSource<Int, CatImage> = CatImagePagingSource()
        get() {
            if (field.invalid) {
                field = repository?.favoriteImagesFactory(
                    needFavorite,
                    needLiked,
                    needWatched,
                    needAlarmed
                ) ?: CatImagePagingSource.listSource(Default.PREVIEW_CAT_IMAGES)
            }
            return field
        }

    val images: StateFlow<PagingData<CatImage>> =
        Pager(PagingConfig(pageSize = 20)) {
            imageSource
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun openedImageSource(image: CatImage): StateFlow<PagingData<CatImage>> {
        return Pager(PagingConfig(pageSize = 1)) {
            object : PagingSource<Int, CatImage>() {
                override fun getRefreshKey(state: PagingState<Int, CatImage>): Int? = null

                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatImage> =
                    LoadResult.Page(listOf(image), null, null)

            }
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

    override fun update(catImage: CatImage) {
        repository?.update(catImage)
    }
}