package com.example

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.utils.ContextHelper

class CatImageAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, p1: Intent?) {

        if (p1?.action != ContextHelper.CAT_IMAGE_ALARM) return

        val bundle: Bundle = p1.getBundleExtra(ContextHelper.CAT_IMAGE_ALARM)?: return
        val image: CatImage = bundle.getParcelable(ContextHelper.CAT_IMAGE_PARCEL)?: return

        ContextHelper.createNotification(context, image)
        Log.i("VVV", "Notify Send")
    }
}