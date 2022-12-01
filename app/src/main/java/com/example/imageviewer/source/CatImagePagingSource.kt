package com.example.imageviewer.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.source.web.ApiConst
import com.example.imageviewer.source.web.WebService

class CatImagePagingSource(
    private val service: WebService,
    private val breedId: String? = null,
    private val categoryId: Int? = null
) : PagingSource<Int, CatImage>() {

    init {
        Log.i("SearchFragment", "ImagePager create with $breedId, $categoryId")
    }

    override fun getRefreshKey(state: PagingState<Int, CatImage>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.nextKey?.minus(1) ?: page.prevKey?.plus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatImage> {
        Log.i("SearchFragment", "ImagePager load")
        val page = params.key ?: ApiConst.FIRST_PAGE_INDEX
        val onPage =
            if (params.loadSize < ApiConst.PAGE_MAX_SIZE) params.loadSize
            else ApiConst.PAGE_MAX_SIZE
        val list = service.searchPublicImages(page, onPage, categoryId, breedId)
            ?: return LoadResult.Error(Exception("WebService getNewPublicImages($page, $onPage) Error"))
        Log.i("SearchFragment", "ImagePager loaded $list")
        val nextKey = if (list.size < onPage) null else page + 1
        val prevKey = if (page <= ApiConst.FIRST_PAGE_INDEX) null else page - 1
        return LoadResult.Page(list, prevKey, nextKey)
    }
}