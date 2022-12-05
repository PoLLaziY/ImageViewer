package com.example.imageviewer.source.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.source.ImageSource

interface DbService {
    suspend fun insert(catImage: List<CatImage>)
    suspend fun update(catImage: CatImage)
    suspend fun clean(): Int
    val allImageSource: ImageSource
    val favoriteImageSource: ImageSource
    val dbUpdate: LiveData<Unit>
}

class DbServiceImpl(val dao: CatImageDao) : DbService {

    override suspend fun insert(catImage: List<CatImage>) {
        dao.insert(catImage)
    }

    override suspend fun update(catImage: CatImage) {
        dao.update(catImage)
    }

    override suspend fun clean(): Int {
        return dao.cleanCash()
    }

    override val allImageSource: ImageSource
        get() = object : ImageSource {
            override suspend fun searchImages(
                page: Int,
                onPage: Int,
                query: String?
            ): List<CatImage>? {
                return dao.allCachedImages(page, onPage)
            }
        }

    override val favoriteImageSource: ImageSource
        get() = object : ImageSource {
            override suspend fun searchImages(
                page: Int,
                onPage: Int,
                query: String?
            ): List<CatImage>? {
                return dao.favoriteImages(page, onPage)
            }
        }

    override val dbUpdate: LiveData<Unit> = dao.updateListener().map { Unit }
}