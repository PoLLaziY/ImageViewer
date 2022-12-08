package com.example.imageviewer.source

import com.example.imageviewer.domain.Breed
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.domain.Category
import com.example.imageviewer.source.data.DbService
import com.example.imageviewer.source.web.WebService
import com.example.imageviewer.utils.SearchAlgorithm
import com.example.imageviewer.utils.SearchAlgorithmImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageRepository(
    private val webService: WebService,
    private val dbService: DbService
) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        webService.onImagesLoaded = {
            scope.launch {
                dbService.insert(it)
            }
        }

        dbService.dbUpdate.observeForever {
            favoriteImagesFactory?.invalidate()
        }
    }

    val allCategories: StateFlow<List<Category>?> =
        MutableStateFlow<List<Category>?>(null).also { state ->
            scope.launch {
                state.emit(webService.getAllCategories() ?: emptyList())
            }
        }

    val allBreeds: StateFlow<List<Breed>?> = MutableStateFlow<List<Breed>?>(null).also { state ->
        scope.launch {
            state.emit(webService.getAllBreeds() ?: emptyList())
        }
    }

    var queryForSearch: String? = null
        set(value) {
            if (field == value) return
            field = value
            foundImagesSourceFactory?.invalidate()
        }

    fun update(image: CatImage) {
        scope.launch {
            dbService.update(image)
        }
    }

    var favoriteImagesFactory: CatImagePagingSource? = null
        get() {
            if (field == null || field?.invalid != false) {
                field = CatImagePagingSource(
                    dbService.favoriteImageSource,
                    query = null
                )
                return field
            }
            return field
        }

    var newImagesSourceFactory: CatImagePagingSource? = null
        get() {
            if (field == null || field?.invalid != false) {
                field = CatImagePagingSource(
                    webService.imageSource(searchAlgorithm),
                    dbService.allImageSource,
                    query = null
                )
                return field
            }
            return field
        }

    var foundImagesSourceFactory: CatImagePagingSource? = null
        get() {
            if (field == null || field?.invalid != false) {
                field = CatImagePagingSource(
                    webService.imageSource(searchAlgorithm),
                    dbService.allImageSource,
                    query = queryForSearch
                )
                return field
            }
            return field
        }


    private suspend fun waitInitParams(): Job =
        suspendCoroutine { coroutine ->
            val job = Job()
            CoroutineScope(coroutine.context + job).launch {
                var breedsInit = (allBreeds.value != null)
                var categoriesInit = (allCategories.value != null)

                this.launch {
                    allCategories.filter { it != null }.collect {
                        categoriesInit = true
                        if (breedsInit) coroutine.resume(job)
                    }
                }

                this.launch {
                    allBreeds.filter { it != null }.collect {
                        breedsInit = true
                        if (categoriesInit) coroutine.resume(job)
                    }
                }
            }
        }

    private val searchAlgorithm: SearchAlgorithm = SearchAlgorithmImpl(allBreeds, allCategories)
}