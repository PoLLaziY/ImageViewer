package com.example.imageviewer.web

import com.example.imageviewer.BuildConfig
import com.example.imageviewer.domain.Breed
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.domain.Category
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface WebService {
    suspend fun getNewPublicImages(page: Int, onPage: Int): List<CatImage>?
    suspend fun searchPublicImages(
        page: Int,
        onPage: Int,
        categoryIds: Int?,
        breedId: String?
    ): List<CatImage>?

    suspend fun getImage(id: String): CatImage?
    suspend fun getAllBreeds(): List<Breed>?
    suspend fun getAllCategories(): List<Category>?
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

    override suspend fun getNewPublicImages(page: Int, onPage: Int): List<CatImage>? {
        return service.getAllPublicImages(apiKey = ApiConst.KEY, page = page, onPage = onPage)
            .body()
    }

    override suspend fun getImage(id: String): CatImage? {
        return service.getImage(imageId = id, apiKey = ApiConst.KEY).body()
    }

    override suspend fun getAllBreeds(): List<Breed>? {
        return service.getAllBreeds(ApiConst.KEY, ApiConst.PAGE_MAX_SIZE, ApiConst.FIRST_PAGE_INDEX)
            .body()
    }

    override suspend fun getAllCategories(): List<Category>? {
        return service.getAllCategories(
            ApiConst.KEY,
            ApiConst.PAGE_MAX_SIZE,
            ApiConst.FIRST_PAGE_INDEX
        ).body()
    }

    override suspend fun searchPublicImages(
        page: Int,
        onPage: Int,
        categoryIds: Int?,
        breedId: String?
    ): List<CatImage>? {
        return when {
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
    }
}