package com.example.imageviewer.view

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.web.WebConst
import com.example.imageviewer.web.WebService

class CatImagePagingSource(
    private val service: WebService,
    private val query: String?
) : PagingSource<Int, CatImage>() {
    override fun getRefreshKey(state: PagingState<Int, CatImage>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.nextKey?.minus(1) ?: page.prevKey?.plus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatImage> {
        if (query == null) {
            val page = params.key ?: 1
            val onPage =
                if (params.loadSize < WebConst.API_PAGE_MAX_SIZE) params.loadSize
                else WebConst.API_PAGE_MAX_SIZE
            val list = service.getNewPublicImages(page, onPage) ?: return LoadResult.Error(Exception("WebService getNewPublicImages($page, $onPage) Error"))
            val nextKey = if (list.size < onPage) null else page+1
            val prevKey = if (page <= 1) null else page-1
            return LoadResult.Page(list, prevKey, nextKey)
        }
        return LoadResult.Error(Exception("PagingSource fun load not implemented"))
    }
}