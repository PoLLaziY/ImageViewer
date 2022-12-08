package com.example.imageviewer.source

import com.example.imageviewer.domain.CatImage

interface ImageSource {
    suspend fun searchImages(
        page: Int,
        onPage: Int,
        query: String? = null
    ): List<CatImage>?
}