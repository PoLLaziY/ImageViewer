package com.example.imageviewer.domain

import org.junit.Assert
import org.junit.Test


internal class CatImageTest {

    @Test
    fun `get new snapshot on change`() {
        val catImage = CatImage()

        val snapshot1 = catImage.snapshot
        catImage.alarmTime = 2
        val snapshot2 = catImage.snapshot

        Assert.assertNotEquals(snapshot1, snapshot2)
    }
}