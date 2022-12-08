package com.example.imageviewer.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.source.web.ApiConst

class CatImagePagingSource(
    private val sources: ImageSource? = null,
    private val query: String? = null,
    private inline val onSourceReturnNull: (() -> Unit)? = null
) : PagingSource<Int, CatImage>() {

    init {
        if (sources == null) this.invalidate()
    }

    override fun getRefreshKey(state: PagingState<Int, CatImage>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.nextKey?.minus(1) ?: page.prevKey?.plus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatImage> {
        val page = params.key ?: ApiConst.FIRST_PAGE_INDEX
        val onPage =
            if (params.loadSize < ApiConst.PAGE_MAX_SIZE) params.loadSize
            else ApiConst.PAGE_MAX_SIZE
        val list: List<CatImage> = sources?.getImages(page, onPage, query)
            ?: return LoadResult.Error<Int, CatImage>(Exception()).apply {
                onSourceReturnNull?.invoke()
            }
        val nextKey = if (list.size < onPage) null else page + 1
        val prevKey = if (page <= ApiConst.FIRST_PAGE_INDEX) null else page - 1
        return LoadResult.Page(list, prevKey, nextKey)
    }
}