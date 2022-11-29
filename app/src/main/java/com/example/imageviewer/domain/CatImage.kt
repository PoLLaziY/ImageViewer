package com.example.imageviewer.domain

import com.google.gson.annotations.SerializedName


data class CatImage(
    @SerializedName("breeds") var breeds: ArrayList<Breed> = arrayListOf(),
    @SerializedName("categories") var categories: ArrayList<Category> = arrayListOf(),
    @SerializedName("id") var id: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("width") var width: Int? = null,
    @SerializedName("height") var height: Int? = null
)