package com.behere.location_based_reminder.model.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo ORDER BY todoCreatedTime DESC")
    fun getAllTodo(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo WHERE todoId=(:id)")
    fun getCurrentTodo(id: Int): Todo

    @Query("SELECT * FROM todo WHERE todoNotiOn=(:on)")
    fun getOnTodo(on: Boolean): LiveData<List<Todo>>

    @Query("SELECT todoPlace FROM todo ORDER BY todoCreatedTime DESC")
    fun getPlaces(): List<String>

    @Query("SELECT todoPlace FROM todo WHERE todoNotiOn=(:on)")
    fun getOnPlace(on: Boolean): List<String>

    @Update
    fun updateTodo(todo: Todo)

    @Delete
    fun deleteTodo(todo:Todo)

    @Insert
    fun addTodo(todo: Todo)

    @Insert
    fun addTodos(todos: List<Todo>)
}