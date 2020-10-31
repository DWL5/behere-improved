package com.behere.location_based_reminder.di

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.behere.location_based_reminder.model.LocationManager
import com.google.android.gms.location.R

class AppApplication : Application() {
    lateinit var apiContainer: ApiContainer
    private val ANDROID_CHANNEL_ID = "my.kotlin.application.test200812"
    private var notificationManager: NotificationManager? = null

    private enum class NotificationMode {
        normal, issue
    }

    override fun onCreate() {
        super.onCreate()
        apiContainer = ApiContainer(this)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //http://apis.data.go.kr/B553077/api/open/sdsc/storeListInRadius?radius=100&cx=126.935539&cy=37.555512&ServiceKey=oHRM3LmzGC5b3wvDiHHv71TdYCcs50DkzlRlvBah21L5rtIjzDeNugOGm5mSvmOIlxdKerwEn2x8iA1M45hpeQ%3D%3D&numOfRows=100&pageNo=4&type=jso
        LocationManager.getInstance(applicationContext).startLocationUpdates()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ANDROID_CHANNEL_ID,
                "TODO Service",
                NotificationManager.IMPORTANCE_NONE
            )
            notificationManager!!.createNotificationChannel(channel)
        }
    }

    /*private fun createStickNotification(notificationMode: NotificationMode) {
        //Create notification object
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
    }*/

}