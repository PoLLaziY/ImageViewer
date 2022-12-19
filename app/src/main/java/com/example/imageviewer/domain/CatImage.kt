package com.example.imageviewer.domain

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "images")
data class CatImage(
    @Ignore @SerializedName("breeds") var breeds: ArrayList<Breed> = arrayListOf(),
    @Ignore @SerializedName("categories") var categories: ArrayList<Category> = arrayListOf(),
    @SerializedName("id") @PrimaryKey var id: String = "",
    @SerializedName("url") @ColumnInfo(name = "image_url") var url: String? = null,
    @ColumnInfo(name = "is_favorite") var favorite: Long = 0,
    @ColumnInfo(name = "liked") var liked: Long = 0,
    @ColumnInfo(name = "watched") var watched: Long = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        arrayListOf(),
        arrayListOf(),
        parcel.readString()?:"",
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(url)
        parcel.writeLong(favorite)
        parcel.writeLong(liked)
        parcel.writeLong(watched)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CatImage> {
        override fun createFromParcel(parcel: Parcel): CatImage {
            return CatImage(parcel)
        }

        override fun newArray(size: Int): Array<CatImage?> {
            return arrayOfNulls(size)
        }
    }
}