package com.behere.location_based_reminder.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.behere.location_based_reminder.KEY_FREQUENCY
import com.behere.location_based_reminder.KEY_RADIUS
import com.behere.location_based_reminder.LocationUpdatingService
import com.behere.location_based_reminder.R
import com.behere.location_based_reminder.di.AppApplication
import kotlinx.android.synthetic.main.fragment_setting_place.*
import kotlinx.android.synthetic.main.fragment_setting_place.close_btn
import kotlinx.android.synthetic.main.fragment_setting_place.ok_btn
import kotlinx.android.synthetic.main.fragment_setting_place_notification.*
import kotlin.math.min

class SettingPlaceNotificationFragment : DialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting_place_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        close_btn.setOnClickListener {
            dismiss()
//            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        ok_btn.setOnClickListener {
            dismiss()
//            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        meter100.setOnClickListener(meterOnClickListener)
        meter300.setOnClickListener(meterOnClickListener)
        meter500.setOnClickListener(meterOnClickListener)
        meter1000.setOnClickListener(meterOnClickListener)

        minute1.setOnClickListener(minOnClickListener)
        minute3.setOnClickListener(minOnClickListener)
        minute5.setOnClickListener(minOnClickListener)
        minute10.setOnClickListener(minOnClickListener)

        val r = AppApplication.prefs.getInt(KEY_RADIUS, 1000)
        val m = AppApplication.prefs.getInt(KEY_FREQUENCY, 10)

        var selectedR = meter1000
        when (r) {
            100 -> {
                selectedR = meter100
            }
            300 -> {
                selectedR = meter300
            }
            500 -> {
                selectedR = meter500
            }
            1000 -> {
                selectedR = meter1000
            }
        }

        selectedR.setTextColor(resources.getColor(R.color.colorAccent))
        var selectedM = minute10
        when (m) {
            1 -> {
                selectedM = minute1
            }
            3 -> {
                selectedM = minute3
            }
            5 -> {
                selectedM = minute5
            }
            10 -> {
                selectedM = minute10
            }
        }
        selectedM.setTextColor(resources.getColor(R.color.colorAccent))
    }

    override fun onDestroy() {
        super.onDestroy()

        val view = (activity as MainActivity?)!!.findViewById<View>(R.id.more_btn)
        view.visibility = View.VISIBLE
        view.isSelected = false
        view.setBackgroundResource(R.drawable.more)

        (activity as MainActivity?)!!.findViewById<View>(R.id.more_content).visibility =
            View.VISIBLE
    }

    private val meterOnClickListener = object : View.OnClickListener {
        override fun onClick(p0: View?) {
            if (p0 !is TextView) return
            meter100.setTextColor(resources.getColor(R.color.grey4))
            meter300.setTextColor(resources.getColor(R.color.grey4))
            meter500.setTextColor(resources.getColor(R.color.grey4))
            meter1000.setTextColor(resources.getColor(R.color.grey4))
            p0.setTextColor(resources.getColor(R.color.colorAccent))
            when (p0?.id) {
                R.id.meter100 ->
                    AppApplication.prefs.setInt(KEY_RADIUS, 100)
                R.id.meter300 ->
                    AppApplication.prefs.setInt(KEY_RADIUS, 300)
                R.id.meter500 ->
                    AppApplication.prefs.setInt(KEY_RADIUS, 500)
                R.id.meter1000 ->
                    AppApplication.prefs.setInt(KEY_RADIUS, 1000)
            }
            Toast.makeText(context, "값이 변경 되었습니다.", Toast.LENGTH_LONG).show()
        }
    }

    private val minOnClickListener = object : View.OnClickListener {
        override fun onClick(p0: View?) {
            if (p0 !is TextView) return
            minute1.setTextColor(resources.getColor(R.color.grey4))
            minute3.setTextColor(resources.getColor(R.color.grey4))
            minute5.setTextColor(resources.getColor(R.color.grey4))
            minute10.setTextColor(resources.getColor(R.color.grey4))
            p0.setTextColor(resources.getColor(R.color.colorAccent))
            when (p0?.id) {
                R.id.minute1 -> {
                    AppApplication.prefs.setInt(KEY_FREQUENCY, 1)
                }

                R.id.minute3 -> {
                    AppApplication.prefs.setInt(KEY_FREQUENCY, 3)
                }

                R.id.minute5 -> {
                    AppApplication.prefs.setInt(KEY_FREQUENCY, 5)
                }

                R.id.minute10 -> {
                    AppApplication.prefs.setInt(KEY_FREQUENCY, 10)
                }
            }
            requireActivity().stopService(
                Intent(
                    context,
                    LocationUpdatingService::class.java
                )
            )
            (requireActivity().application as AppApplication).startService()
            Toast.makeText(context, "값이 변경 되었습니다.", Toast.LENGTH_LONG).show()
        }
    }
}