package com.behere.location_based_reminder

import android.content.Context
import android.content.SharedPreferences

const val KEY_RADIUS = "key_radius"
const val KEY_FREQUENCY = "key_frequency"
class PreferenceUtil (context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("place_find_settings", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return prefs.getInt(key, defValue)
    }

    fun setInt(key: String, i: Int) {
        prefs.edit().putInt(key, i).apply()
    }
}