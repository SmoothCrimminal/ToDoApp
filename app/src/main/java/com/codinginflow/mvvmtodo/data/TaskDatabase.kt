package com.codinginflow.mvvmtodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codinginflow.mvvmtodo.data.DAO.TaskDao

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}