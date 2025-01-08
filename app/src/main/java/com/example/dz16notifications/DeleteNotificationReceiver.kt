package com.example.dz16notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DeleteNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        //Log.d("aaa", "onReceive: reciver starts")
       // Log.d("aaa", "onReceive: ${intent?.action}")
        if (intent?.action == "com.kodiiiofc.urbanuniversity.natificationslecture.ACTION_DISMISS_NOTIFICATION") {
           // Log.d("aaa", "onReceive: ${intent.action}")

            val notificationID = intent.getIntExtra("NOTIFICATION_ID", -1)
            //Log.d("aaa", "onReceive: $notificationID")
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationID)
        }
    }
}