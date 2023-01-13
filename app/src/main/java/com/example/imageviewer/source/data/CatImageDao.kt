package com.example.imageviewer.source.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.imageviewer.domain.CatImageSnapshot

@Dao
interface CatImageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(catImage: CatImageSnapshot)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list: List<CatImageSnapshot>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(catImage: CatImageSnapshot)

    @Query("SELECT * FROM images ORDER BY id LIMIT :onPage OFFSET :page")
    suspend fun allCachedImages(page: Int, onPage: Int): List<CatImageSnapshot>

    @Query(
        "SELECT * FROM images " +
                "WHERE is_favorite > :favoriteMoreThan " +
                "AND liked > :likedMoreThan " +
                "AND watched > :watchedMoreThan " +
                "AND alarm_time > :alarmTimeMore " +
                "ORDER BY alarm_time, is_favorite DESC, liked DESC, watched DESC " +
                "LIMIT :onPage OFFSET :page"
    )
    suspend fun getImages(
        page: Int,
        onPage: Int,
        favoriteMoreThan: Long = -1,
        likedMoreThan: Long = -1,
        watchedMoreThan: Long = -1,
        alarmTimeMore: Long = -1
    ): List<CatImageSnapshot>

    @Query("DELETE FROM images WHERE liked = 0 and is_favorite = 0")
    suspend fun cleanCash(): Int

    @Query("DELETE FROM images WHERE liked = 1 or is_favorite = 1")
    suspend fun cleanFavorite(): Int

    @Query("SELECT is_favorite FROM images LIMIT 1")
    fun updateListener(): LiveData<Long?>
}