package com.example.imageviewer.source.data

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CatImageRoomDbTest {
    var context: Context? = null
    var db: CatImageRoomDb? = null

    @Before
    fun initTests() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            context ?: return,
            CatImageRoomDb::class.java
        )
            .allowMainThreadQueries().build()
    }

    @After
    fun closeDb() {
        db?.close()
    }

    @Test
    fun getDao() {
        val link1 = db?.catImageDao() ?: assert(false)
        val link2 = db?.catImageDao() ?: assert(false)

        assert(link1 === link2)
    }
}