package com.example.imageviewer.source.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imageviewer.domain.CatImage

@Database(entities = [CatImage::class], exportSchema = false, version = 2)
abstract class CatImageRoomDb: RoomDatabase() {
    abstract fun catImageDao(): CatImageDao
}