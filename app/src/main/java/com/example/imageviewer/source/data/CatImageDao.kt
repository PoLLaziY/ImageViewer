package com.example.imageviewer.source.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.imageviewer.domain.CatImage

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

    @Query("SELECT * FROM images WHERE is_favorite > 0 ORDER BY is_favorite DESC LIMIT :onPage OFFSET :page")
    suspend fun favoriteImages(page: Int, onPage: Int): List<CatImage>

    @Query("SELECT * FROM images WHERE liked > 0 ORDER BY liked DESC LIMIT :onPage OFFSET :page")
    suspend fun likedImages(page: Int, onPage: Int): List<CatImage>

    @Query("DELETE FROM images WHERE liked = 0 and is_favorite = 0")
    suspend fun cleanCash(): Int

    @Query("DELETE FROM images WHERE is_favorite = 1")
    suspend fun cleanFavorite(): Int

    @Query("SELECT is_favorite FROM images LIMIT 1")
    fun updateListener(): LiveData<Long?>
}