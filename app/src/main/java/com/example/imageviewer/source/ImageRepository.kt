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
) : ImageStateUpdater {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        webService.loadedImages.observeForever {
            scope.launch {
                dbService.insert(it)
            }
        }
        dbService.dbUpdate.observeForever {
            favoriteImagesFactory?.invalidate()
        }
    }

    private val allCategories: StateFlow<List<Category>?> by lazy {
        val flow = MutableStateFlow<List<Category>?>(null)
        scope.launch {
            flow.emit(webService.getAllCategories() ?: emptyList())
        }
        return@lazy flow
    }

    private val allBreeds: StateFlow<List<Breed>?> by lazy {
        val flow = MutableStateFlow<List<Breed>?>(null)
        scope.launch {
            flow.emit(webService.getAllBreeds() ?: emptyList())
        }
        return@lazy flow
    }

    private val searchAlgorithm: SearchAlgorithm = SearchAlgorithmImpl(allBreeds, allCategories)

    private var foundImagesSourceFactory: CatImagePagingSource? = null
    fun foundImagesSourceFactory(query: String?): CatImagePagingSource {
        foundImagesSourceFactory = CatImagePagingSource(
            webService.imageSource(searchAlgorithm),
            query = query
        )
        return foundImagesSourceFactory!!
    }

    private var favoriteImagesFactory: CatImagePagingSource? = null
    fun favoriteImagesFactory(
        favorite: Boolean = false,
        liked: Boolean = false,
        watched: Boolean = false,
        alarmed: Boolean = false
    ): CatImagePagingSource {
        favoriteImagesFactory = CatImagePagingSource(
            dbService.imageSource(favorite, liked, watched, alarmed)
        )
        return favoriteImagesFactory!!
    }

    private var newImagesSourceFactory: CatImagePagingSource? = null
    fun newImagesSourceFactory(onLoadNull: (() -> Unit)? = null): CatImagePagingSource  {
        newImagesSourceFactory = CatImagePagingSource(
            webService.imageSource(searchAlgorithm),
            onSourceReturnNull = onLoadNull
        )
        return newImagesSourceFactory!!
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

    override fun update(catImage: CatImage) {
        scope.launch {
            dbService.update(catImage)
        }
    }

    suspend fun getImages(
        page: Int,
        onPage: Int,
        favoriteMoreThan: Long = -1,
        likedMoreThan: Long = -1,
        watchedMoreThan: Long = -1,
        alarmTimeMore: Long = -1
    ): List<CatImage> {
        return dbService.getImages(page, onPage, favoriteMoreThan, likedMoreThan, watchedMoreThan, alarmTimeMore)
    }
}