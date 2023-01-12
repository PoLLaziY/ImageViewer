package com.example.imageviewer.di

import android.content.Context
import com.example.imageviewer.di.module.CoreModule
import com.example.imageviewer.di.module.ViewModelsModule
import com.example.imageviewer.source.ImageRepository
import com.example.imageviewer.viewModel.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreModule::class, ViewModelsModule::class])
abstract class AppComponent {

    abstract val repository: ImageRepository

    abstract val homeScreenViewModel: HomeScreenViewModel
    abstract val searchScreenViewModel: SearchScreenViewModel
    abstract val favoriteScreenViewModel: FavoriteScreenViewModel

    @Component.Factory
    abstract class Factory {
        abstract fun create(@BindsInstance context: Context): AppComponent
    }
}

