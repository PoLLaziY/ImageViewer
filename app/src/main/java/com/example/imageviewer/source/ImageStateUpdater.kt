package com.example.imageviewer.source

import com.example.imageviewer.domain.CatImage
import java.util.*

interface ImageStateUpdater {
    fun updateFavorite(image: CatImage) {
        if (image.favorite > 0) image.favorite = 0
        else image.favorite = Date().time
        update(image)
    }
    fun updateLiked(image: CatImage) {
        if (image.liked > 0) image.liked = 0
        else image.liked = Date().time
        update(image)
    }
    fun updateWatched(image: CatImage) {
        if (image.watched > 0) return
        image.watched = Date().time
        update(image)
    }

    fun update(catImage: CatImage)
}