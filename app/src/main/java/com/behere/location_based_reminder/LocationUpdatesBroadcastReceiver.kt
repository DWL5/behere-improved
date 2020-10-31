package com.behere.location_based_reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.behere.loc_based_reminder.data.response.Item
import com.behere.location_based_reminder.di.AppApplication
import com.behere.location_based_reminder.model.LocationManager
import com.behere.location_based_reminder.model.TodoRepository
import com.behere.location_based_reminder.ui.MapActivity
import com.google.android.gms.location.LocationResult
import java.util.*
import java.util.concurrent.Executors

private const val TAG = "LUBroadcastReceiver"

const val FIND_ACTION = "com.behere.loc_based_reminder.FIND_ACTION"
const val NOTI_GROUP = "com.behere.loc_based_reminder.NOTI_GROUP"


private val STICK_NOTIFICATION_ID = 1
private val EVENT_SUMMARY_ID = 0
private val EVENT_NOTIFICATION_ID = 9

class LocationUpdatesBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive() context:$context, intent:$intent")
        val todoRepository = TodoRepository.getInstance(
            context,
            Executors.newSingleThreadExecutor()
        )
        
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.e("우진", "Boot Complete 브로드캐스트 실행")

            context.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //오레오 이상은 백그라운드로 실행하면 강제 종료 위험 있음 -> 포그라운드 실행해야
                    it?.startForegroundService(
                        Intent(
                            context?.applicationContext,
                            LocationUpdatingService::class.java
                        )
                    )
                    Log.e("우진", "API 레벨 26 이상")
                } else {
                    //백그라운드 실행에 제약 없음
                    it?.startService(
                        Intent(
                            context?.applicationContext,
                            LocationUpdatingService::class.java
                        )
                    )
                    Log.e("우진", "API 레벨 25 이하")
//                    }
                }
            }
        } else if (intent.action == ACTION_PROCESS_UPDATES) {
            LocationResult.extractResult(intent)?.let { locationResult ->
                for (location in locationResult!!.locations) {
                    Log.e(
                        TAG,
                        "[location(FLC)] latitude: ${location.latitude} longitude: ${location.longitude}"
                    )
                    val application = context.applicationContext as AppApplication
                    val queries = todoRepository.getPlace(true)

                    if (queries.isNullOrEmpty()) return
                    val temp = queries.toTypedArray()
                    application.apiContainer.storeListServiceRepository
                        .getToDoStoreListNearBy(
                            radius = 100,
                            cx = location.longitude.toFloat(),
                            cy = location.latitude.toFloat(),
                            numOfRows = 1000,
                            success = { map ->
                                Log.e(TAG, "Success Result $map")
                                var id = EVENT_NOTIFICATION_ID
                                for (value in map) {
                                    Log.e(TAG, "noti : ${value.key}")
                                    with(NotificationManagerCompat.from(application.applicationContext))
                                    {
                                        notify(
                                            id,
                                            setEventNotification(
                                                context,
                                                location,
                                                value.key,
                                                value.value,
                                                id
                                            )!!.build()
                                        )
                                    }
                                    id += 1
                                }
                                if (map.size > 1) {
                                    val summaryNotification = NotificationCompat.Builder(
                                        context,
                                        ANDROID_CHANNEL_ID
                                    )
                                        .setContentTitle("근접 알림")
                                        .setContentText("${map.size}개의 알림이 있습니다.")
                                        .setSmallIcon(R.drawable.alarm)
                                        .setGroup(NOTI_GROUP)
                                        .setGroupSummary(true)
                                        .build()

                                    with(NotificationManagerCompat.from(context)) {
                                        notify(EVENT_SUMMARY_ID, summaryNotification)
                                    }
                                }
                            },
                            fail = {
                                Log.e(TAG, "Fail Result $it")
                            },
                            queries = *temp
                        )
                }
            }
        }
    }

    private fun setEventNotification(
        context: Context,
        location: Location,
        q: String,
        items: ArrayList<Item>,
        id: Int
    ): NotificationCompat.Builder {
        Log.d("NOTI", "setEventNotification() called")
        //알림 클릭 시, 앱 진입
        val intent = Intent(context, MapActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = FIND_ACTION
            putParcelableArrayListExtra("items", items)
            putExtra("lat", location.latitude)
            putExtra("lng", location.longitude)
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Set notification content
        return NotificationCompat.Builder(context, ANDROID_CHANNEL_ID)
            .setSmallIcon(R.drawable.alarm)
            .setContentTitle(q)
            .setContentText("할 일 설정 장소 $q 가 인접한 곳에 있습니다.")
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setGroup(NOTI_GROUP)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
    }

    companion object {
        const val ACTION_PROCESS_UPDATES =
            "com.behere.location_based_reminder.action" +
                    "PROCESS_UPDATES"
    }
}