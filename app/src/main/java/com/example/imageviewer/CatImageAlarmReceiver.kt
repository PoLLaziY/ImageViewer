package com.example.imageviewer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.imageviewer.domain.CatImageSnapshot
import com.example.imageviewer.view.values.ContextHelper

class CatImageAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, p1: Intent?) {
        Log.i("VVV", "BroadcastStart")

        if (p1?.action != ContextHelper.CAT_IMAGE_ALARM) return

        val bundle: Bundle = p1.getBundleExtra(ContextHelper.CAT_IMAGE_ALARM)?: return
        val image: CatImageSnapshot = bundle.getParcelable(ContextHelper.CAT_IMAGE_PARCEL)?: return

        if (image.alarmTime == 0L) return

        ContextHelper.updateNotification(context, image)
    }
}