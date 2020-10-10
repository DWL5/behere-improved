package com.behere.location_based_reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.LocationResult
import java.util.*
import java.util.concurrent.Executors

private const val TAG = "LUBroadcastReceiver"
class LocationUpdatesBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        Log.d(TAG, "onReceive() context:$context, intent:$intent")
        if (intent.action == ACTION_PROCESS_UPDATES) {
            LocationResult.extractResult(intent)?.let { locationResult ->
                /*val locations = locationResult.locations.map { location ->
                    MyLocationEntity(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        foreground = isAppInForeground(context),
                        date = Date(location.time)
                    )
                }
                if (locations.isNotEmpty()) {
                    LocationRepository.getInstance(context, Executors.newSingleThreadExecutor())
                        .addLocations(locations)
                }*/
            }
        }
    }
    companion object {
        const val ACTION_PROCESS_UPDATES =
            "com.behere.location_based_reminder.action" +
                    "PROCESS_UPDATES"
    }
}