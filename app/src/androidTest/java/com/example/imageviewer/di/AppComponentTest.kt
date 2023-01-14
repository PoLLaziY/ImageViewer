package com.example.imageviewer.di

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppComponentTest {
    var context: Context? = null
    var appComponent: AppComponent? = null

    @Before
    fun initForTests() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        appComponent = DaggerAppComponent.factory().create(context?: return)
    }

    @Test
    fun getRepository() {
        val link1 = appComponent?.repository ?: assert(false)
        val link2 = appComponent?.repository ?: assert(false)
        assert(link1 === link2)
    }

    @Test
    fun getHomeScreenViewModel() {
        val link1 = appComponent?.homeScreenViewModel ?: assert(false)
        val link2 = appComponent?.homeScreenViewModel ?: assert(false)
        assert(link1 === link2)
    }

    @Test
    fun getSearchScreenViewModel() {
        val link1 = appComponent?.searchScreenViewModel ?: assert(false)
        val link2 = appComponent?.searchScreenViewModel ?: assert(false)
        assert(link1 === link2)
    }

    @Test
    fun getFavoriteScreenViewModel() {
        val link1 = appComponent?.favoriteScreenViewModel ?: assert(false)
        val link2 = appComponent?.favoriteScreenViewModel ?: assert(false)
        assert(link1 === link2)
    }
}