package com.behere.location_based_reminder.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.behere.location_based_reminder.R
import com.behere.location_based_reminder.model.db.Todo
import com.behere.location_based_reminder.viewmodles.TodoViewModel
import kotlinx.android.synthetic.main.activity_main.todo_edit
import kotlinx.android.synthetic.main.fragment_add_todo.*

class AddTodoFragment : DialogFragment() {

    private val todoViewModel by lazy {
        ViewModelProviders.of(this).get(TodoViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //키보드 on
        todo_edit.requestFocus()

        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )

        save_btn.setOnClickListener {
            todoViewModel.insert(
                Todo(
                    todoTitle = todo_edit.text.toString(),
                    todoPlace = "B",
                    todoNotiOn = true,
                    todoCreatedTime = System.currentTimeMillis()
                )
            )

            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        close_btn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        filter_btn.setOnClickListener {
            val settingPlaceFragment = SettingPlaceFragment()
            parentFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.anim.slide_up, 0, 0, 0)
                ?.add(android.R.id.content, settingPlaceFragment)
                ?.commit()

            view.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()

        //키보드 off
        todo_edit.clearFocus()

        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}