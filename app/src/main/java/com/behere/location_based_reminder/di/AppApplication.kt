package com.behere.location_based_reminder.di

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.behere.location_based_reminder.ANDROID_CHANNEL_ID
import com.behere.location_based_reminder.LocationUpdatingService
import com.behere.location_based_reminder.PreferenceUtil
import com.behere.location_based_reminder.model.LocationManager
import com.google.android.gms.location.R

class AppApplication : Application() {
    lateinit var apiContainer: ApiContainer
    private var notificationManager: NotificationManager? = null

    companion object { lateinit var prefs: PreferenceUtil }


    private enum class NotificationMode {
        normal, issue
    }

    override fun onCreate() {
        super.onCreate()
        apiContainer = ApiContainer(this)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //http://apis.data.go.kr/B553077/api/open/sdsc/storeListInRadius?radius=100&cx=126.935539&cy=37.555512&ServiceKey=oHRM3LmzGC5b3wvDiHHv71TdYCcs50DkzlRlvBah21L5rtIjzDeNugOGm5mSvmOIlxdKerwEn2x8iA1M45hpeQ%3D%3D&numOfRows=100&pageNo=4&type=jso
        prefs = PreferenceUtil(applicationContext)
        createNotificationChannel()
        startService()
    }

    public fun startService() {
        applicationContext.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //오레오 이상은 백그라운드로 실행하면 강제 종료 위험 있음 -> 포그라운드 실행해야
                it?.startForegroundService(
                    Intent(
                        applicationContext?.applicationContext,
                        LocationUpdatingService::class.java
                    )
                )
                Log.e("우진", "API 레벨 26 이상")
            } else {
                //백그라운드 실행에 제약 없음
                it?.startService(
                    Intent(
                        applicationContext?.applicationContext,
                        LocationUpdatingService::class.java
                    )
                )
                Log.e("우진", "API 레벨 25 이하")
//                    }
            }
        }
    }

    private fun createNotificationChannel() {
        Log.d("NOTI", "createNotificationChannel() called")
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