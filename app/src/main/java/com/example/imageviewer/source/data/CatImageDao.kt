package com.example.imageviewer.source.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.imageviewer.domain.CatImage
import kotlinx.coroutines.flow.StateFlow
import retrofit2.http.GET
import java.util.concurrent.Flow

@Dao
interface CatImageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(catImage: CatImage)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list: List<CatImage>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(catImage: CatImage)
    @Query("SELECT * FROM images ORDER BY id LIMIT :onPage OFFSET :page")
    suspend fun allCachedImages(page: Int, onPage: Int): List<CatImage>
    @Query("SELECT * FROM images WHERE is_favorite = 1 ORDER BY id LIMIT :onPage OFFSET :page")
    suspend fun favoriteImages(page: Int, onPage: Int): List<CatImage>
    @Query("SELECT * FROM images WHERE liked = 1 ORDER BY id LIMIT :onPage OFFSET :page")
    suspend fun likedImages(page: Int, onPage: Int): List<CatImage>
    @Query("DELETE FROM images WHERE liked = 0 and is_favorite = 0")
    suspend fun cleanCash(): Int
    @Query("DELETE FROM images WHERE is_favorite = 1")
    suspend fun cleanFavorite(): Int
    @Query("SELECT is_favorite FROM images LIMIT 1")
    fun updateListener(): LiveData<Int>
}