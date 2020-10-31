package com.behere.location_based_reminder.ui

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
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
import com.behere.location_based_reminder.LocationUpdatesBroadcastReceiver
import com.behere.location_based_reminder.R
import com.behere.location_based_reminder.model.db.Todo
import com.behere.location_based_reminder.viewmodles.TodoViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_todo_info.*
import kotlinx.android.synthetic.main.adapter_todo_list.*
import kotlinx.android.synthetic.main.layout_custom_bottom_dialog_place.*
import kotlinx.android.synthetic.main.layout_custom_bottom_dialog_place.view.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    lateinit var mAdapter: TodoListAdapter
    lateinit var moreFragment:MoreFragment

    private val todoViewModel by lazy {
        ViewModelProviders.of(this).get(TodoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        initializeViews()

        if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION), 0)
        }

        if (checkSelfPermission(ACCESS_BACKGROUND_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(ACCESS_BACKGROUND_LOCATION), 0)
        }
    }

    private fun initializeViews() {
        val todoDataList = todoViewModel.todoLiveData
        val todoList = todo_list
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(todo_list)
        mAdapter = TodoListAdapter(this, ArrayList())
        mAdapter.setOnItemSelectedListener(object : TodoListAdapter.ItemSelectedListener {
            override fun onSelectedListener(todo: Todo) {
                showCustomViewDialogEditMode(BottomSheet(LayoutMode.WRAP_CONTENT), todo)
                mAdapter.notifyDataSetChanged()
            }
        })

        todoList.adapter = mAdapter
        todoList.layoutManager = LinearLayoutManager(this)
        todoDataList.observe(this, Observer {
            mAdapter.submitData(it)
        })

        moreFragment = MoreFragment()
        more_btn.setOnClickListener {
            if(!it.isSelected){
//                it.setBackgroundResource(R.drawable.more_click)
                supportFragmentManager.beginTransaction().add(R.id.more_content, moreFragment).commit()
                it.isSelected = true
            }
            else{
//                it.setBackgroundResource(R.drawable.more)
                removeFragment()
                it.isSelected = false
            }
//            showCustomViewDialog(BottomSheet(LayoutMode.WRAP_CONTENT))
        }
    }

    fun removeFragment(){
        supportFragmentManager.beginTransaction().remove(moreFragment).commit()
    }


    private fun showCustomViewDialog(dialogBehavior: DialogBehavior = ModalDialog) {
        val dialog = MaterialDialog(this, dialogBehavior).show {
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
            }
            //lifecycleOwner(this@MainActivity)
            //debugMode(debugMode)
        }

        val customView = dialog.getCustomView()
        val checkBox = customView.findViewById<CheckBox>(R.id.checkbox_location)
        checkBox.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
//                customView.edit_location.isEnabled = p1
//                customView.view_location_dim.visibility = View.INVISIBLE
                customView.layout_location.visibility = View.VISIBLE
            } else {
//                customView.edit_location.isEnabled = p1
//                customView.view_location_dim.visibility = View.VISIBLE
                customView.layout_location.visibility = View.GONE
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

    private fun showCustomViewDialogEditMode(
        dialogBehavior: DialogBehavior = ModalDialog,
        todo: Todo
    ) {
        val dialog = MaterialDialog(this, dialogBehavior).show {
            title(R.string.todo)
            customView(
                R.layout.layout_custom_bottom_dialog_place,
                scrollable = false,
                horizontalPadding = true
            )
            positiveButton(R.string.edit) { dialog ->
                todo.todoTitle = edit_todo.text.toString()
                if (checkbox_location.isChecked) {
                    Log.e("우진", "IS CHECKED")
                    todo.todoPlace = edit_location.text.toString()
                    todo.todoNotiOn = switch_notication_on.isChecked
                } else {
                    todo.todoPlace = ""
                    todo.todoNotiOn = false
                }

                todoViewModel.update(todo)

                todoViewModel.todoLiveData
                dialog.dismiss()
            }

            negativeButton(R.string.cancel) { dialog ->
                dialog.dismiss()
            }
            //lifecycleOwner(this@MainActivity)
            //debugMode(debugMode)

        }

        val customView = dialog.getCustomView()
        val checkBox = customView.findViewById<CheckBox>(R.id.checkbox_location)
        checkBox.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                customView.layout_location.visibility = View.VISIBLE
            } else {
                customView.layout_location.visibility = View.GONE
            }
        }

        if (todo.todoPlace?.isEmpty()!!) {
            customView.layout_location.visibility = View.GONE
        } else {
            customView.layout_location.visibility = View.VISIBLE

            customView.checkbox_location.isChecked = true

            customView.edit_location.setText(todo.todoPlace)
            customView.switch_notication_on.isChecked = todo.todoNotiOn!!
        }
    }

//    private fun setViewWithData(todo: Todo) {
//        edit_todo.setText(todo.todoTitle)
//        val isLocationEmpty = todo.todoPlace?.length ?: -1
//        if (isLocationEmpty != -1) {
//            checkbox_location.isChecked = true
//            edit_location.setText(todo.todoPlace)
//            switch_notication_on.isChecked = todo.todoNotiOn!!
//        }
//    }

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
                var startServiceIntent: Intent? = null
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