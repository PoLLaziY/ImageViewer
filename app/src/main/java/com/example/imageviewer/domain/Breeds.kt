package com.example.imageviewer.domain

import com.google.gson.annotations.SerializedName


data class Breeds(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
)