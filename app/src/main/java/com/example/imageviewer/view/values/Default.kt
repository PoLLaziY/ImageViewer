package com.example.imageviewer.view.values

import com.example.imageviewer.domain.CatImage

object Default {
    val PREVIEW_CAT_IMAGE = CatImage(
        "asd",
        "https://i.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI",
        1,
        1,
        1,
        1
    )

    val PREVIEW_CAT_IMAGES: List<CatImage> = mutableListOf<CatImage>().apply {
        repeat(20) {
            this.add(PREVIEW_CAT_IMAGE)
        }
    }
}