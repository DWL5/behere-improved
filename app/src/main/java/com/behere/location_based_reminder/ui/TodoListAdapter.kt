package com.behere.location_based_reminder.ui

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.behere.location_based_reminder.R
import com.behere.location_based_reminder.model.db.Todo
import kotlinx.android.synthetic.main.adapter_todo_list.view.*

class TodoListAdapter(private val context: Context, private val todoDataList: List<Todo>) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.adapter_todo_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.todoTitle.text = todoDataList[position].todoTitle

        if (todoDataList[position].todoPlace.isNullOrEmpty()) {
            holder.todoPlace.visibility = View.GONE
        } else {
            holder.todoPlace.visibility = View.VISIBLE
        }

        holder.checkBox.setOnClickListener {
            if (!it.isSelected) {
                holder.todoTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                it.isSelected = true
            } else {
                holder.todoTitle.paintFlags = Paint.ANTI_ALIAS_FLAG
                it.isSelected = false
            }
        }
    }

    override fun getItemCount(): Int {
        return todoDataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoTitle: TextView = itemView.todo_title_txt
        val todoPlace:ImageView =  itemView.location_img
        val checkBox: CheckBox = itemView.check_box
    }
}