package com.behere.location_based_reminder.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_custom_bottom_dialog_place.*
import kotlinx.android.synthetic.main.layout_custom_bottom_dialog_place.view.*

class MainActivity : AppCompatActivity() {

    lateinit var mAdapter: TodoListAdapter

    private val todoViewModel by lazy {
        ViewModelProviders.of(this).get(TodoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        initializeViews()
    }

    private fun initializeViews() {
        val todoDataList = todoViewModel.todoLiveData
        val todoList = todo_list
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(todo_list)
        mAdapter = TodoListAdapter(this, ArrayList())
        todoList.adapter = mAdapter
        todoList.layoutManager = LinearLayoutManager(this)
        todoDataList.observe(this, Observer {
            mAdapter.submitData(it)
        })

        more_btn.setOnClickListener {
            showCustomViewDialog(BottomSheet(LayoutMode.WRAP_CONTENT))
        }
    }


    private fun showCustomViewDialog(dialogBehavior: DialogBehavior = ModalDialog) {
        val dialog = MaterialDialog(this, dialogBehavior).show {
            title(R.string.todo)
            customView(R.layout.layout_custom_bottom_dialog_place, scrollable = false, horizontalPadding = true)
            positiveButton(R.string.add) { dialog ->
                todoViewModel.insert(Todo(todoTitle = edit_todo.text.toString(), todoPlace = edit_location.text.toString(),
                    todoNotiOn = switch_notication_on.isChecked, todoCreatedTime = System.currentTimeMillis()
                ))
                dialog.dismiss()
            }
            negativeButton(R.string.cancel) {dialog ->
                dialog.dismiss()
            }
            //lifecycleOwner(this@MainActivity)
            //debugMode(debugMode)
        }

        val customView = dialog.getCustomView()
        val checkBox = customView.findViewById<CheckBox>(R.id.checkbox_location)
        checkBox.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                customView.edit_location.isEnabled = p1
                customView.view_location_dim.visibility = View.INVISIBLE
            } else {
                customView.edit_location.isEnabled = p1
                customView.view_location_dim.visibility = View.VISIBLE
            }
        }

        // Setup custom view content

       /* val passwordInput: EditText = customView.findViewById(R.id.password)
        val showPasswordCheck: CheckBox = customView.findViewById(R.id.showPassword)
        showPasswordCheck.setOnCheckedChangeListener { _, isChecked ->
            passwordInput.inputType =
                if (!isChecked) InputType.TYPE_TEXT_VARIATION_PASSWORD else InputType.TYPE_CLASS_TEXT
            passwordInput.transformationMethod =
                if (!isChecked) PasswordTransformationMethod.getInstance() else null
        }*/
    }


    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
        object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                @NonNull recyclerView: RecyclerView,
                @NonNull viewHolder: RecyclerView.ViewHolder,
                @NonNull target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                @NonNull viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                var startServiceIntent : Intent? = null
                val position = viewHolder.adapterPosition
                todoViewModel.delete(mAdapter.getItem(position))
                //mAdapter.notifyItemRemoved(position)

                /*if (todoList.size == 0) {
                    stopService(startServiceIntent)
                    Log.e("주창", "Stop service")
                }*/
            }
        }
}