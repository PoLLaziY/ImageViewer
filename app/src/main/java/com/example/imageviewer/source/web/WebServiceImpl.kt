package com.example.imageviewer.source.web

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imageviewer.BuildConfig
import com.example.imageviewer.domain.Breed
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.domain.Category
import com.example.imageviewer.source.ImageSource
import com.example.imageviewer.utils.SearchAlgorithm
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface WebService {
    suspend fun getImage(id: String): CatImage?
    suspend fun getAllBreeds(): List<Breed>?
    suspend fun getAllCategories(): List<Category>?
    fun imageSource(searchAlgorithm: SearchAlgorithm? = null): ImageSource
    val loadedImages: LiveData<List<CatImage>>
}

class WebServiceImpl : WebService {

    private val interceptor by lazy {
        HttpLoggingInterceptor().apply {
            this.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
            else HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val service by lazy {
        retrofit.create(CatImageRetrofitService::class.java)
    }

    override val loadedImages = MutableLiveData<List<CatImage>>()

    override suspend fun getImage(id: String): CatImage? {
        return try {
            service.getImage(imageId = id, apiKey = ApiConst.KEY).body()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllBreeds(): List<Breed>? {
        return try {
            service.getAllBreeds(ApiConst.KEY, ApiConst.PAGE_MAX_SIZE, ApiConst.FIRST_PAGE_INDEX)
                .body()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllCategories(): List<Category>? {
        return try {
            service.getAllCategories(
                ApiConst.KEY,
                ApiConst.PAGE_MAX_SIZE,
                ApiConst.FIRST_PAGE_INDEX
            ).body()
        } catch (e: Exception) {
            null
        }
    }

    override fun imageSource(searchAlgorithm: SearchAlgorithm?): ImageSource {
        return object : ImageSource {
            override suspend fun getImages(
                page: Int,
                onPage: Int,
                query: String?
            ): List<CatImage>? {
                val breedId = if (query == null) null else searchAlgorithm?.getBreedsFrom(
                    query
                )
                    ?.firstOrNull()?.id
                val categoryId = if (query == null) null else searchAlgorithm?.getCategoriesFrom(
                    query
                )
                    ?.firstOrNull()?.id
                return searchPublicImages(page, onPage, categoryId, breedId)
            }
        }
    }

    suspend fun searchPublicImages(
        page: Int,
        onPage: Int,
        categoryIds: Int?,
        breedId: String?
    ): List<CatImage>? {
        val result = try {
            when {
                categoryIds == null && breedId == null -> service.getAllPublicImages(
                    ApiConst.KEY,
                    onPage,
                    page
                )
                categoryIds == null -> service.searchPublicImages(
                    ApiConst.KEY,
                    onPage,
                    page,
                    breedId!!
                )
                breedId == null -> service.searchPublicImages(
                    ApiConst.KEY,
                    onPage,
                    page,
                    categoryIds
                )
                else -> service.searchPublicImages(
                    ApiConst.KEY,
                    onPage,
                    page,
                    categoryIds,
                    breedId
                )
            }.body()
        } catch (e: Exception) {
            null
        }
        if (result != null) loadedImages.postValue(result)
        return result
    }

}