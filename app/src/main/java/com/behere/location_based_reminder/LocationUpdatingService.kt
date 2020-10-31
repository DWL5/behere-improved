package com.behere.location_based_reminder

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.behere.location_based_reminder.model.LocationManager
class LocationUpdatingService : Service() {
    private val STICK_NOTIFICATION_ID = 1

    companion object {
        var serviceIntent: Intent? = null
    }

    private var notificationManager: NotificationManager? = null


    private enum class NotificationMode {
        normal, issue
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        Log.d("Service", "onCreate() called" )
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Service", "onStartCommand() called" )
        LocationManager.getInstance(context = applicationContext).startLocationUpdates()
        serviceIntent = intent
        //Stick a Notification for Foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createStickNotification(NotificationMode.normal)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        LocationManager.getInstance(context = applicationContext).stopLocationUpdates()
    }

    private fun createStickNotification(notificationMode: NotificationMode) {
        //Create notification object
        Log.d("Service", "createStickNotification() called" )
        var notification: Notification? = null

        when (notificationMode) {
            NotificationMode.normal -> {
                val builder = NotificationCompat.Builder(this, ANDROID_CHANNEL_ID)
                    .setContentTitle("TO DO")
                    .setContentText("위치 정보 사용 중입니다.")
                    .setSmallIcon(R.drawable.location)
                notification = builder.build()
            }
            NotificationMode.issue -> {
                val builder = NotificationCompat.Builder(this, ANDROID_CHANNEL_ID)
                    .setContentTitle("TO DO")
                    .setContentText("위치 권한 추가 허용이 필요합니다.")
                    .setSmallIcon(R.drawable.location)
                notification = builder.build()
            }
        }
        //Service starts with notification pinning
        startForeground(STICK_NOTIFICATION_ID, notification)
    }
}