package com.behere.location_based_reminder.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behere.location_based_reminder.R
import com.behere.location_based_reminder.viewmodles.TodoViewModel
import kotlinx.android.synthetic.main.activity_main.*

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
            val moreFragment = MoreFragment()
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