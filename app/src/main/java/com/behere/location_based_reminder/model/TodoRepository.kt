package com.behere.location_based_reminder.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.behere.location_based_reminder.model.db.Todo
import com.behere.location_based_reminder.model.db.TodoDatabase
import java.util.*
import java.util.concurrent.ExecutorService

class TodoRepository  private constructor(
    private val todoDatabase: TodoDatabase,
    private val executor: ExecutorService
) {
    // Database related fields/methods:
    private val todoDao = todoDatabase.locationDao()

    /**
     * Returns all recorded locations from database.
     */
    fun getAllTodo(): LiveData<List<Todo>> = todoDao.getAllTodo()

    // Not being used now but could in future versions.
    /**
     * Returns specific location in database.
     */
    fun getOnTodo(on: Boolean): LiveData<Todo> = todoDao.getOnTodo(on)

    // Not being used now but could in future versions.
    /**
     * Updates location in database.
     */
    fun updateTodo(todo: Todo) {
        executor.execute {
            todoDao.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        executor.execute {
            todoDao.deleteTodo(todo)
        }
    }

    /**
     * Adds location to the database.
     */
    fun addTodo(myLocationEntity: Todo) {
        executor.execute {
            todoDao.addTodo(myLocationEntity)
        }
    }

    /**
     * Adds list of locations to the database.
     */
    fun addTodos(todos: List<Todo>) {
        executor.execute {
            todoDao.addTodos(todos)
        }
    }
    companion object {
        @Volatile private var INSTANCE: TodoRepository? = null

        fun getInstance(context: Context, executor: ExecutorService): TodoRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TodoRepository(
                    TodoDatabase.getInstance(context),
                    executor)
                    .also { INSTANCE = it }
            }
        }
    }

}