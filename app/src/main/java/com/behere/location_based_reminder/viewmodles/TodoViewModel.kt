package com.behere.location_based_reminder.viewmodles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.behere.location_based_reminder.model.TodoRepository
import com.behere.location_based_reminder.model.db.Todo
import java.util.concurrent.Executors

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoRepository = TodoRepository.getInstance(
        application.applicationContext,
        Executors.newSingleThreadExecutor()
    )

    val todoLiveData = todoRepository.getAllTodo()
    val todoOnLiveData = todoRepository.getOnTodo(true)

    fun insert(todo: Todo) {
        this.todoRepository.addTodo(todo)
    }

    fun update(todo:Todo) {
        todoRepository.updateTodo(todo)
    }

    fun delete(todo:Todo) {

    }
}