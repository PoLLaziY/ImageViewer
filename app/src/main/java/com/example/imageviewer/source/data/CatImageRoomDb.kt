package com.example.imageviewer.source.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.domain.CatImageSnapshot

@Database(entities = [CatImageSnapshot::class], exportSchema = false, version = 3)
abstract class CatImageRoomDb: RoomDatabase() {
    abstract fun catImageDao(): CatImageDao
}