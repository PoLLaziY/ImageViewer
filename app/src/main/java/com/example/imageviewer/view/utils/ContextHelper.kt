package com.example.imageviewer.view.utils

import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.imageviewer.CatImageAlarmReceiver
import com.example.imageviewer.R
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.source.ImageStateUpdater
import com.example.imageviewer.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.net.URL
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ContextHelper {

    const val CAT_IMAGE_PARCEL = "cat_image_parcel"
    const val CAT_IMAGE_ALARM = "cat_image_alarm"
    const val CAT_NOTIFICATION_CHANNEL_ID = "CAT_NOTIFICATION_CHANNEL_ID"
    const val NOTIFICATION_NAME = "AlarmToCatImage"

    fun updateAlarm(context: Context?, image: CatImage?, imageStateUpdater: ImageStateUpdater) {
        if (image == null || context == null) return

        CoroutineScope(Dispatchers.Main).launch {
            val time =
                if (image.alarmTime == 0L) (pickTime(context)?.timeInMillis ?: return@launch)
                else 0

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            image.alarmTime = time
            imageStateUpdater.update(image)

            val intent = Intent(context, CatImageAlarmReceiver::class.java).apply {
                action = CAT_IMAGE_ALARM
                val bundle = Bundle().apply {
                    this.putParcelable(CAT_IMAGE_PARCEL, image.snapshot)
                }
                this.putExtra(CAT_IMAGE_ALARM, bundle)
            }

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    image.id.hashCode(),
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )

            if (image.alarmTime == 0L) alarmManager.cancel(pendingIntent)
            else alarmManager.set(AlarmManager.RTC, time, pendingIntent)

            Log.i("VVV", "Broadcast create")
        }
    }


    fun loadImage(context: Context?, image: CatImage?) {
        if (image == null || context == null) return
        val resolver = context.contentResolver

        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, image.id)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri: Uri = resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        ) ?: return

        CoroutineScope(Dispatchers.IO).launch {
            val stream: OutputStream? = resolver.openOutputStream(uri)
            stream?.write(URL(image.url).readBytes())
            stream?.close()

            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun shareImage(context: Context?, image: CatImage?) {
        if (image == null || context == null) return
        val resolver = context.contentResolver

        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/gif"

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, image.id)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/gif")
        val uri: Uri = resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        ) ?: return

        CoroutineScope(Dispatchers.IO).launch {
            val stream: OutputStream? = resolver.openOutputStream(uri)
            stream?.write(URL(image.url).readBytes())
            stream?.close()

            share.putExtra(Intent.EXTRA_STREAM, uri)
            context.startActivity(Intent.createChooser(share, "Share Image"))
        }
    }

    private suspend fun pickTime(context: Context?): Calendar? {
        return suspendCoroutine {
            if (context == null) it.resume(null)
            else {
                val calendar = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    { _, y, m, d ->
                        TimePickerDialog(
                            context,
                            { _, h, min ->
                                val time = Calendar.getInstance().apply {
                                    set(y, m, d, h, min)
                                }
                                Log.i("AlarmSet", "Alarm send ${time.timeInMillis}")
                                it.resume(time)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE), true
                        ).show()
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
                Log.i("AlarmSet", "AfterPick")
            }
        }
    }

    fun updateNotification(context: Context?, image: CatImage) {
        if (context == null) return

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CAT_NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_LOW
            )

            notificationManager.createNotificationChannel(notificationChannel)

            val notification =
                buildNotification(context, image, null, CAT_NOTIFICATION_CHANNEL_ID)
            notificationManager.notify("CAT_IMAGE_NOTIFY_TAG", 1, notification)

        } else {
            val notification = buildNotification(context, image, null)
            notificationManager.notify("CAT_IMAGE_NOTIFY_TAG", 1, notification)
        }
    }

    private fun buildNotification(
        context: Context,
        image: CatImage,
        bitmap: Bitmap?,
        channelId: String? = null
    ): Notification {
        val intent = Intent(context, MainActivity::class.java)
            .apply {
                putExtra(CAT_IMAGE_PARCEL, image.snapshot)
            }

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        return if (channelId != null) NotificationCompat.Builder(context, channelId)
            .setContentTitle("Reminder")
            .setContentInfo("Reminder")
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.setSmallIcon(IconCompat.createWithResource(context, R.mipmap.ic_launcher))
                }
            }
            .setLargeIcon(
                bitmap ?: BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        else NotificationCompat.Builder(context)
            .setContentTitle("Reminder")
            .setContentInfo("Reminder")
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.setSmallIcon(IconCompat.createWithResource(context, R.mipmap.ic_launcher))
                }
            }
            .setLargeIcon(
                bitmap ?: BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }
}