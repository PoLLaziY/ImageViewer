package com.example.imageviewer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.imageviewer.view.utils.ContextHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmOnReboot : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (context.applicationContext is App) {
            CoroutineScope(Dispatchers.IO).launch {
                val repository = App.appComponent.repository
                val list =
                    repository.getImages(page = 0, onPage = 100, alarmTimeMore = 0)
                list.forEach { image ->
                    ContextHelper.updateAlarm(context, image, repository)
                }
            }
        }
    }
}