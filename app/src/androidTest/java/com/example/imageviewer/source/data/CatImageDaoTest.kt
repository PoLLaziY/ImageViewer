package com.example.imageviewer.source.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.imageviewer.view.values.Default
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CatImageDaoTest {
    var context: Context? = null
    var db: CatImageRoomDb? = null
    var dao: CatImageDao? = null
    //val main = newSingleThreadContext("MyUIThread")

    @Before
    fun initTests() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            context ?: return,
            CatImageRoomDb::class.java
        )
            .allowMainThreadQueries().build()
        dao = db?.catImageDao() ?: return
        //Dispatchers.setMain(main)
    }

    @After
    fun closeDb() {
        runBlocking {
            dao?.cleanCash()
            dao?.cleanFavorite()
            db?.close()
        }

    }

    @Test
    fun insertImage() {
        val catImageSnapShot = Default.PREVIEW_CAT_IMAGE.snapshot
        runBlocking {
            dao?.insert(catImageSnapShot)
            val image = dao?.getImages(0, 1)?.get(0) ?: assert(false)
            assert(catImageSnapShot == image)
        }

    }

    @Test
    fun insertList() {
        val list = Default.PREVIEW_CAT_IMAGES.map { it.snapshot }
        runBlocking {
            dao?.insert(list)
            val images = dao?.getImages(0, list.size)
            assert(list.all { images?.contains(it) ?: false })
        }

    }


    @Test
    fun update() {
        val image1 = Default.PREVIEW_CAT_IMAGE.snapshot
        val image2 = image1.copy(favorite = 2)
        runBlocking {
            dao?.insert(image1)
            dao?.update(image2)
            val images = dao?.getImages(0, 2)
            assert(images?.size == 1 && images[0] == image2)
        }

    }

    @Test
    fun insertCantUpdate() {
        val image1 = Default.PREVIEW_CAT_IMAGE.snapshot
        val image2 = image1.copy(favorite = 2)
        runBlocking {
            dao?.insert(image1)
            dao?.insert(image2)
            val images = dao?.getImages(0, 2)
            assert(images?.size == 1 && images[0] == image1)
        }

    }

    @Test
    fun cleanCash() {
        val image = Default.PREVIEW_CAT_IMAGE.snapshot
        val images = mutableListOf(
            image.copy(id = "1", favorite = 1, liked = 1),
            image.copy(id = "2", favorite = 1, liked = 0),
            image.copy(id = "3", favorite = 0, liked = 1),
            image.copy(id = "4", favorite = 0, liked = 0),
        )
        runBlocking {
            dao?.insert(images)
            dao?.cleanCash()
            val images2 = dao?.getImages(0, 50)
            assert(images2?.size == 3 && images2.all { it.favorite > 0 || it.liked > 0 })
        }

    }

    @Test
    fun cleanFavorite() {
        val image = Default.PREVIEW_CAT_IMAGE.snapshot
        val images = mutableListOf(
            image.copy(id = "1", favorite = 1, liked = 1),
            image.copy(id = "2", favorite = 1, liked = 0),
            image.copy(id = "3", favorite = 0, liked = 1),
            image.copy(id = "4", favorite = 0, liked = 0),
        )
        runBlocking {
            dao?.insert(images)
            dao?.cleanFavorite()
            val images2 = dao?.getImages(0, 50)
            assert(images2?.size == 1 && images2.all { it.favorite < 1 && it.liked < 1 })
        }

    }

    @Test(timeout = 2000)
    fun updateListener() {
        val image = Default.PREVIEW_CAT_IMAGE.snapshot
        val images = mutableListOf(
            image.copy(id = "1", favorite = 1, liked = 1),
            image.copy(id = "2", favorite = 1, liked = 0),
            image.copy(id = "3", favorite = 0, liked = 1),
            image.copy(id = "4", favorite = 0, liked = 0),
        )
        var send = false
        var remove = false
        var update = false

        val listener = dao?.updateListener()
        var observer: Observer<Long?>? = null

        observer = Observer<Long?> {
            runBlocking {
                if (dao?.getImages(0, 50)?.size == 4) send = true
                if (dao?.getImages(0, 50)?.size == 3 && send) remove = true
                if (dao?.getImages(0, 50)?.size == 3 && remove) update = true
                if (send && remove && update) {
                    listener?.removeObserver(observer ?: return@runBlocking)
                    assert(true)
                }

            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            listener?.observeForever(observer)
        }

        runBlocking {
            send = true
            dao?.insert(image)
            dao?.cleanCash()
            dao?.update(images[0].copy(liked = 0, favorite = 0))
            CoroutineScope(Dispatchers.Main).launch {
                listener?.removeObserver(observer)
            }
        }
    }

    @Test
    fun testGetImage() {
        val image = Default.PREVIEW_CAT_IMAGE.snapshot
        val images = mutableListOf(
            image.copy(id = "1", favorite = 1, liked = 1, alarmTime = 0, watched = 0),
            image.copy(id = "2", favorite = 1, liked = 0, alarmTime = 0, watched = 0),
            image.copy(id = "3", favorite = 0, liked = 1, alarmTime = 0, watched = 0),
            image.copy(id = "4", favorite = 0, liked = 0, alarmTime = 0, watched = 1),
            image.copy(id = "5", favorite = 0, liked = 0, alarmTime = 1, watched = 0)
        )

        runBlocking {
            dao?.insert(images)
            val listAll = dao?.getImages(0, 50)
            val listFavorite = dao?.getImages(0, 50, favoriteMoreThan = 0)
            val listLiked = dao?.getImages(0, 50, likedMoreThan = 0)
            val listWatched = dao?.getImages(0, 50, watchedMoreThan = 0)
            val listAlarmed = dao?.getImages(0, 50, alarmTimeMore = 0)

            assert(
                listAll?.size == 5 &&
                        listFavorite?.size == 2 && listFavorite.all { it.favorite > 0 } &&
                        listLiked?.size == 2 && listLiked.all { it.liked > 0 } &&
                        listWatched?.size == 1 && listWatched.all { it.watched > 0 } &&
                        listAlarmed?.size == 1 && listAlarmed.all { it.alarmTime > 0 }
            )
        }
    }
}