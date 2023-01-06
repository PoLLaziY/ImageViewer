package com.example.imageviewer.di.module

import android.content.Context
import androidx.room.Room
import com.example.imageviewer.source.ImageRepository
import com.example.imageviewer.source.data.CatImageDao
import com.example.imageviewer.source.data.CatImageRoomDb
import com.example.imageviewer.source.data.DbService
import com.example.imageviewer.source.data.DbServiceImpl
import com.example.imageviewer.source.web.WebService
import com.example.imageviewer.source.web.WebServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule {

    @Singleton
    @Provides
    fun provideImageRepository(webService: WebService, dbService: DbService): ImageRepository =
        ImageRepository(webService, dbService)

    @Singleton
    @Provides
    fun provideWebService(): WebService = WebServiceImpl()

    @Singleton
    @Provides
    fun provideDbService(dao: CatImageDao): DbService = DbServiceImpl(dao)

    @Singleton
    @Provides
    fun provideCatImageDao(roomDb: CatImageRoomDb): CatImageDao = roomDb.catImageDao()

    @Singleton
    @Provides
    fun provideCatImageRoomDb(context: Context): CatImageRoomDb =
        Room.databaseBuilder(context, CatImageRoomDb::class.java, "cat_image_db")
            .fallbackToDestructiveMigration()
            .build()
}