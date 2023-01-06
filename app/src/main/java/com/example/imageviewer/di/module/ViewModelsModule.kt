package com.example.imageviewer.di.module

import com.example.imageviewer.source.ImageRepository
import com.example.imageviewer.viewModel.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelsModule {
    @Singleton
    @Provides
    fun provideHomeScreenViewModel(repository: ImageRepository): HomeScreenViewModel =
        HomeScreenViewModel(repository = repository)

    @Singleton
    @Provides
    fun provideSearchScreenViewModel(repository: ImageRepository): SearchScreenViewModel =
        SearchScreenViewModel(repository = repository)

    @Singleton
    @Provides
    fun provideFavoriteScreenViewModel(repository: ImageRepository): FavoriteScreenViewModel =
        FavoriteScreenViewModel(repository = repository)
}