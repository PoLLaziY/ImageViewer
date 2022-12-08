package com.example.di.module

import com.example.imageviewer.source.ImageRepository
import com.example.imageviewer.viewModel.FavoriteFragmentViewModel
import com.example.imageviewer.viewModel.ImagePagerViewModel
import com.example.imageviewer.viewModel.SearchFragmentViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelsModule {
    @Singleton
    @Provides
    fun provideRandomImageViewModel(repository: ImageRepository): ImagePagerViewModel =
        ImagePagerViewModel(repository)

    @Singleton
    @Provides
    fun provideSearchFragmentViewModel(repository: ImageRepository): SearchFragmentViewModel =
        SearchFragmentViewModel(repository)

    @Singleton
    @Provides
    fun provideFavoriteFragmentViewModel(repository: ImageRepository): FavoriteFragmentViewModel =
        FavoriteFragmentViewModel(repository)
}