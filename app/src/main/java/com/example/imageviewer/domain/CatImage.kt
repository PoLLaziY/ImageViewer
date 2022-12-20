package com.example.imageviewer.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "images")
@Parcelize
data class CatImage(
    @Ignore @SerializedName("breeds") var breeds: ArrayList<Breed> = arrayListOf(),
    @Ignore @SerializedName("categories") var categories: ArrayList<Category> = arrayListOf(),
    @SerializedName("id") @PrimaryKey var id: String = "",
    @SerializedName("url") @ColumnInfo(name = "image_url") var url: String? = null,
    @ColumnInfo(name = "is_favorite") var favorite: Long = 0,
    @ColumnInfo(name = "liked") var liked: Long = 0,
    @ColumnInfo(name = "watched") var watched: Long = 0,
    @ColumnInfo(name = "alarm_time") var alarmTime: Long = 0L
) : Parcelable