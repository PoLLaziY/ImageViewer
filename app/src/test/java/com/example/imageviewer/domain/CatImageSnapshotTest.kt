package com.example.imageviewer.domain

import org.junit.Test

internal class CatImageSnapshotTest {

    @Test
    fun `get one current CatImage`() {

        val snapshot =
            CatImageSnapshot(id = "", url = "", favorite = 0, liked = 0, alarmTime = 0, watched = 0)

        val current1 = snapshot.current

        current1.alarmTime = 1

        val current2 = snapshot.current

        assert(current1 === current2)
    }


    @Test
    fun `get one current CatImage, then get new Snapshot`() {

        val snapshot1 =
            CatImageSnapshot(id = "", url = "", favorite = 0, liked = 0, alarmTime = 0, watched = 0)

        val current = snapshot1.current

        current.alarmTime = 1

        val snapshot2 = current.snapshot

        assert(snapshot1 !== snapshot2)
    }
}