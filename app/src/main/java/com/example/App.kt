package com.example

import android.app.Application
import com.example.di.AppComponent
import com.example.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        innerAppComponent = DaggerAppComponent.factory().create(this)
    }

    companion object {
        private var innerAppComponent: AppComponent? = null

        val appComponent: AppComponent
            get() = innerAppComponent!!
    }
}