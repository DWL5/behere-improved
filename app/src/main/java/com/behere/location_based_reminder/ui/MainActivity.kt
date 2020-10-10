package com.behere.location_based_reminder.ui

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.behere.location_based_reminder.R
import com.behere.location_based_reminder.viewmodles.TodoViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


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
        val adapter = TodoListAdapter(this, ArrayList())
        adapter.submitData(todoDataList.value)
        todoList.adapter = adapter
        todoList.layoutManager = LinearLayoutManager(this)
        todoDataList.observe(this, Observer {
            adapter.submitData(it)
        })
        
        add_btn.setOnClickListener {
            val addTodoFragment = AddTodoFragment()
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_up, 0, 0, 0)
                .add(R.id.more_content, moreFragment)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()

        more_content.visibility = View.VISIBLE
    }
}