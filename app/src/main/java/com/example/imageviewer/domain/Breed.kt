package com.example.imageviewer.domain

import com.google.gson.annotations.SerializedName


data class Breed(
    @SerializedName("id") var id: String? = null,
    @SerializedName("alt_names") var altNames: String? = null,
    @SerializedName("life_span") var lifeSpan: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("origin") var origin: String? = null,
    @SerializedName("reference_image_id") var referenceImageId: String? = null,
    @SerializedName("temperament") var temperament: String? = null,
    @SerializedName("weight_imperial") var weightImperial: String? = null
)