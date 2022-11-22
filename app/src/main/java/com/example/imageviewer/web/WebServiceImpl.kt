package com.example.imageviewer.web

import com.example.imageviewer.BuildConfig
import com.example.imageviewer.domain.CatImage
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
    suspend fun getImage(id: String): CatImage?
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
            service.getAllPublicImages(apiKey = WebConst.apiKey, page = page, onPage = onPage)
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
            service.getImage(imageId = id, apiKey = WebConst.apiKey)
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
}