package com.example.imageviewer.source.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.source.ImageSource

interface DbService {
    suspend fun insert(catImage: List<CatImage>)
    suspend fun update(catImage: CatImage)
    suspend fun clean(): Int
    suspend fun getImages(
        page: Int,
        onPage: Int,
        favoriteMoreThan: Long = -1,
        likedMoreThan: Long = -1,
        watchedMoreThan: Long = -1,
        alarmTimeMore: Long = -1
    ): List<CatImage>

    fun allImageSource(): ImageSource
    fun imageSource(
        favorite: Boolean = false,
        liked: Boolean = false,
        watched: Boolean = false,
        alarmed: Boolean = false
    ): ImageSource

    val dbUpdate: LiveData<Unit>
}

class DbServiceImpl(private val dao: CatImageDao) : DbService {

    override suspend fun insert(catImage: List<CatImage>) {
        dao.insert(catImage)
    }

    override suspend fun update(catImage: CatImage) {
        dao.update(catImage)
    }

    override suspend fun clean(): Int {
        return dao.cleanCash()
    }

    override suspend fun getImages(
        page: Int,
        onPage: Int,
        favoriteMoreThan: Long,
        likedMoreThan: Long,
        watchedMoreThan: Long,
        alarmTimeMore: Long
    ): List<CatImage> {
        return dao.getImages(
            page,
            onPage,
            favoriteMoreThan,
            likedMoreThan,
            watchedMoreThan,
            alarmTimeMore
        )
    }

    override fun allImageSource(): ImageSource {
        return object : ImageSource {
            override suspend fun getImages(
                page: Int,
                onPage: Int,
                query: String?
            ): List<CatImage>? {
                return dao.allCachedImages(page, onPage)
            }
        }
    }

    override fun imageSource(
        favorite: Boolean,
        liked: Boolean,
        watched: Boolean,
        alarmed: Boolean
    ): ImageSource {
        return object : ImageSource {
            override suspend fun getImages(
                page: Int,
                onPage: Int,
                query: String?
            ): List<CatImage>? {
                return dao.getImages(
                    page, onPage,
                    favoriteMoreThan = if (favorite) 0 else -1,
                    likedMoreThan = if (liked) 0 else -1,
                    watchedMoreThan = if (watched) 0 else -1,
                    alarmTimeMore = if (alarmed) 0 else -1
                )
            }

        }
    }

    override val dbUpdate: LiveData<Unit> by lazy {
        dao.updateListener().map {}
    }
}