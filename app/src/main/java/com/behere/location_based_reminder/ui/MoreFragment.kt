package com.behere.location_based_reminder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.behere.location_based_reminder.R
import com.behere.location_based_reminder.viewmodles.TodoViewModel
import kotlinx.android.synthetic.main.fragment_more.*


class MoreFragment : DialogFragment() {

    private val todoViewModel by lazy {
        ViewModelProviders.of(this).get(TodoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //장소 알림 설정 버튼
        set_location_notification_btn.setOnClickListener {
            val settingPlaceNotificationFragment = SettingPlaceNotificationFragment()
            parentFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.anim.slide_up, 0, 0, 0)
                ?.add(android.R.id.content, settingPlaceNotificationFragment)
                ?.commit()

            (activity as MainActivity?)!!.findViewById<View>(R.id.more_content).visibility = View.GONE
            (activity as MainActivity?)!!.findViewById<View>(R.id.more_btn).visibility = View.GONE

            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        //TASK 등록 버튼
        add_todo_button.setOnClickListener {
            val addTodoFragment = AddTodoFragment()
            parentFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.anim.slide_up, 0, 0, 0)
                ?.add(android.R.id.content, addTodoFragment)
                ?.commit()

            (activity as MainActivity?)!!.findViewById<View>(R.id.more_content).visibility = View.GONE
            (activity as MainActivity?)!!.findViewById<View>(R.id.more_btn).visibility = View.GONE

            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }

}