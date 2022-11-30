package com.example.imageviewer.web

import android.util.Log
import com.example.imageviewer.BuildConfig
import com.example.imageviewer.domain.Breed
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.domain.Category
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
        return suspendCoroutine { coroutine ->
            service.getAllPublicImages(apiKey = ApiConst.KEY, page = page, onPage = onPage)
                .enqueue(object : Callback<List<CatImage>> {
                    override fun onResponse(
                        call: Call<List<CatImage>>,
                        response: Response<List<CatImage>>
                    ) {
                        coroutine.resume(response.body())
                    }

                    override fun onFailure(call: Call<List<CatImage>>, t: Throwable) {
                        coroutine.resume(null)
                    }
                })
        }
    }

    override suspend fun getImage(id: String): CatImage? {
        return suspendCoroutine { coroutine ->
            service.getImage(imageId = id, apiKey = ApiConst.KEY)
                .enqueue(object : Callback<CatImage> {
                    override fun onResponse(call: Call<CatImage>, response: Response<CatImage>) {
                        coroutine.resume(response.body())
                    }

                    override fun onFailure(call: Call<CatImage>, t: Throwable) {
                        coroutine.resume(null)
                    }
                })
        }
    }

    override suspend fun getAllBreeds(): List<Breed>? {
        return suspendCoroutine { coroutine ->
            service.getAllBreeds(ApiConst.KEY, ApiConst.PAGE_MAX_SIZE, ApiConst.FIRST_PAGE_INDEX)
                .enqueue(object : Callback<List<Breed>> {
                    override fun onResponse(
                        call: Call<List<Breed>>,
                        response: Response<List<Breed>>
                    ) {
                        coroutine.resume(response.body())
                    }

                    override fun onFailure(call: Call<List<Breed>>, t: Throwable) {
                        coroutine.resume(null)
                    }
                })
        }
    }

    override suspend fun getAllCategories(): List<Category>? {
        return suspendCoroutine { coroutine ->
            service.getAllCategories(
                ApiConst.KEY,
                ApiConst.PAGE_MAX_SIZE,
                ApiConst.FIRST_PAGE_INDEX
            )
                .enqueue(object : Callback<List<Category>> {
                    override fun onResponse(
                        call: Call<List<Category>>,
                        response: Response<List<Category>>
                    ) {
                        coroutine.resume(response.body())
                    }

                    override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                        coroutine.resume(null)
                    }
                })
        }
    }

    override suspend fun searchPublicImages(
        page: Int,
        onPage: Int,
        categoryIds: Int?,
        breedId: String?
    ): List<CatImage>? {
        return suspendCoroutine { coroutine ->
            val callback = object : Callback<List<CatImage>> {
                override fun onResponse(
                    call: Call<List<CatImage>>,
                    response: Response<List<CatImage>>
                ) {
                    coroutine.resume(response.body())
                }

                override fun onFailure(call: Call<List<CatImage>>, t: Throwable) {
                    coroutine.resume(null)
                }
            }

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
                    breedId)
            }.enqueue(callback)
        }
    }
}