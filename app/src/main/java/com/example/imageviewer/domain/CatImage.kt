package com.example.imageviewer.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "images")
data class CatImage(
    @SerializedName("breeds") var breeds: ArrayList<Breed> = arrayListOf(),
    @SerializedName("categories") var categories: ArrayList<Category> = arrayListOf(),
    @SerializedName("id") @PrimaryKey var id: String = "",
    @SerializedName("url") @ColumnInfo(name = "image_url") var url: String? = null,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false,
    @ColumnInfo(name = "liked") var liked: Boolean = false
)