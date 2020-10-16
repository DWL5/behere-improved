package com.behere.location_based_reminder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.behere.location_based_reminder.R
import kotlinx.android.synthetic.main.fragment_setting_place.*

class SettingPlaceFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting_place, container, false)
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
    }

    override fun onDestroy() {
        super.onDestroy()

        val view = (activity as MainActivity?)!!.findViewById<View>(R.id.more_btn)
        view.visibility = View.VISIBLE
        view.isSelected = false
        view.setBackgroundResource(R.drawable.more)

        (activity as MainActivity?)!!.findViewById<View>(R.id.more_content).visibility = View.VISIBLE
    }
}