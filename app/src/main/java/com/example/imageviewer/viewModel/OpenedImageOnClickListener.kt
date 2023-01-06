package com.example.imageviewer.viewModel

import android.content.Context
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.source.ImageStateUpdater
import com.example.imageviewer.view.values.ALARM
import com.example.imageviewer.view.values.FAVORITE
import com.example.imageviewer.view.values.LIKE
import com.example.imageviewer.view.values.ContextHelper

interface OpenedImageOnClickListener: ImageStateUpdater {

    fun onClick(context: Context, key: String, catImage: CatImage?) {
        if (catImage == null) return
        when (key) {
            FAVORITE -> updateFavorite(catImage)
            LIKE -> updateLiked(catImage)
            ALARM -> {
                ContextHelper.updateAlarm(context, catImage, this)
            }
        }
    }
}