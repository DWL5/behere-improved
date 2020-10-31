package com.behere.location_based_reminder.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.behere.location_based_reminder.R
import com.behere.location_based_reminder.model.db.Todo
import com.behere.location_based_reminder.viewmodles.TodoViewModel
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.android.synthetic.main.layout_custom_bottom_dialog_place.*
import kotlinx.android.synthetic.main.layout_custom_bottom_dialog_place.view.*


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

            (activity as MainActivity?)!!.findViewById<View>(R.id.more_content).visibility =
                View.GONE
            (activity as MainActivity?)!!.findViewById<View>(R.id.more_btn).visibility = View.GONE

            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        //TASK 등록 버튼
        add_todo_button.setOnClickListener {
//            val addTodoFragment = AddTodoFragment()
//            parentFragmentManager
//                ?.beginTransaction()
//                ?.setCustomAnimations(R.anim.slide_up, 0, 0, 0)
//                ?.add(android.R.id.content, addTodoFragment)
//                ?.commit()
//
//            (activity as MainActivity?)!!.findViewById<View>(R.id.more_content).visibility = View.GONE
//            (activity as MainActivity?)!!.findViewById<View>(R.id.more_btn).visibility = View.GONE
//
//            parentFragmentManager.beginTransaction().remove(this).commit()

            showCustomViewDialog(BottomSheet(LayoutMode.WRAP_CONTENT))
        }
    }

    private fun showCustomViewDialog(dialogBehavior: DialogBehavior = ModalDialog) {
        val dialog = context?.let {
            MaterialDialog(it, dialogBehavior).show {
                title(R.string.todo)
                customView(
                    R.layout.layout_custom_bottom_dialog_place,
                    scrollable = false,
                    horizontalPadding = true
                )
                positiveButton(R.string.add) { dialog ->
                    todoViewModel.insert(
                        Todo(
                            todoTitle = edit_todo.text.toString(),
                            todoPlace = edit_location.text.toString(),
                            todoNotiOn = switch_notication_on.isChecked,
                            todoCreatedTime = System.currentTimeMillis()
                        )
                    )

                    todoViewModel.todoLiveData
                    dialog.dismiss()
                }

                negativeButton(R.string.cancel) { dialog ->
                    dialog.dismiss()

                    (activity as MainActivity).removeFragment()
                }
            }
        }

        val customView = dialog?.getCustomView()
        val checkBox = customView?.findViewById<CheckBox>(R.id.checkbox_location)
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener { p0, p1 ->
                if (p1) {
                    customView.layout_location.visibility = View.VISIBLE
                } else {
                    customView.layout_location.visibility = View.GONE
                }
            }
        }
    }
}