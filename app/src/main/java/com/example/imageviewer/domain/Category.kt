package com.example.imageviewer.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null
): Parcelable