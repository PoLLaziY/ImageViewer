package com.example.imageviewer.domain

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

//Mutable Object with States
class CatImage(
    val id: String = "",
    val url: String? = null,
    favorite: Long,
    liked: Long,
    watched: Long,
    alarmTime: Long,
    val breeds: ArrayList<Breed>? = null,
    val categories: ArrayList<Category>? = null
) {
    var favorite: Long by mutableStateOf(favorite)
    var liked: Long by mutableStateOf(liked)
    var watched: Long by mutableStateOf(watched)
    var alarmTime: Long by mutableStateOf(alarmTime)

    val snapshot: CatImageSnapshot
        get() = CatImageSnapshot(
            breeds,
            categories,
            id,
            url,
            favorite,
            liked,
            watched,
            alarmTime,
            this
        )
}

//Immutable Object
@Entity(tableName = "images")
@Parcelize
data class CatImageSnapshot @JvmOverloads constructor(
    @Ignore @SerializedName("breeds") val breeds: ArrayList<Breed>? = null,
    @Ignore @SerializedName("categories") val categories: ArrayList<Category>? = null,
    @PrimaryKey(autoGenerate = false) @SerializedName("id") val id: String,
    @ColumnInfo(name = "image_url") @SerializedName("url") val url: String?,
    @ColumnInfo(name = "is_favorite") val favorite: Long,
    @ColumnInfo(name = "liked") val liked: Long,
    @ColumnInfo(name = "watched") val watched: Long,
    @ColumnInfo(name = "alarm_time") val alarmTime: Long,
    @Ignore @IgnoredOnParcel var _current: CatImage? = null
) : Parcelable {

    @IgnoredOnParcel
    val current: CatImage
        get() {
            if (_current == null) _current =
                CatImage(id, url, favorite, liked, watched, alarmTime, breeds, categories)
            return _current!!
        }
}