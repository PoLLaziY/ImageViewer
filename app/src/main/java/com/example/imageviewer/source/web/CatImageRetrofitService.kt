package com.example.imageviewer.source.web

import com.example.imageviewer.domain.Breed
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.domain.Category
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatImageRetrofitService {

    @GET("images/search")
    suspend fun getAllPublicImages(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int
    ): Response<List<CatImage>>

    @GET("images/search")
    suspend fun searchPublicImages(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int,
        @Query("category_ids") categoryIds: Int,
        @Query("breed_id") breedId: String
    ): Response<List<CatImage>>


    @GET("images/search")
    suspend fun searchPublicImages(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int,
        @Query("breed_id") breedId: String
    ): Response<List<CatImage>>


    @GET("images/search")
    suspend fun searchPublicImages(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int,
        @Query("category_ids") categoryIds: Int
    ): Response<List<CatImage>>

    @GET("images/{image_id}")
    suspend fun getImage(
        @Path("image_id") imageId: String,
        @Query("api_key") apiKey: String
    ): Response<CatImage>

    @GET("breeds")
    suspend fun getAllBreeds(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int,
    ): Response<List<Breed>>

    @GET("categories")
    suspend fun getAllCategories(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int,
    ): Response<List<Category>>
}