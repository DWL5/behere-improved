package com.behere.location_based_reminder.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "todoId") var id: Int = 0,
    @ColumnInfo(name = "todoTitle") var todoTitle: String,
    @ColumnInfo(name = "todoPlace") var todoPlace: String?,
    @ColumnInfo(name = "todoNotiOn") var todoNotiOn: Boolean?,
    @ColumnInfo(name = "todoCreatedTime") var todoCreatedTime: Long?
) {

}
