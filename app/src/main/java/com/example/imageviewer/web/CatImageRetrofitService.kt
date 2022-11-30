package com.example.imageviewer.web

import com.example.imageviewer.domain.Breed
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.domain.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatImageRetrofitService {

    @GET("images/search")
    fun getAllPublicImages(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int
    ): Call<List<CatImage>>

    @GET("images/search")
    fun searchPublicImages(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int,
        @Query("category_ids") categoryIds: Int,
        @Query("breed_id") breedId: String
    ): Call<List<CatImage>>


    @GET("images/search")
    fun searchPublicImages(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int,
        @Query("breed_id") breedId: String
    ): Call<List<CatImage>>


    @GET("images/search")
    fun searchPublicImages(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int,
        @Query("category_ids") categoryIds: Int
    ): Call<List<CatImage>>

    @GET("images/{image_id}")
    fun getImage(
        @Path("image_id") imageId: String,
        @Query("api_key") apiKey: String
    ): Call<CatImage>

    @GET("breeds")
    fun getAllBreeds(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int,
    ): Call<List<Breed>>

    @GET("categories")
    fun getAllCategories(
        @Query("api_key") apiKey: String,
        @Query("limit") onPage: Int,
        @Query("page") page: Int,
    ): Call<List<Category>>
}