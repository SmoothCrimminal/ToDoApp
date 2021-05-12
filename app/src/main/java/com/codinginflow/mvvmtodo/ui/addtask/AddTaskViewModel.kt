package com.codinginflow.mvvmtodo.ui.addtask

import android.widget.DatePicker
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codinginflow.mvvmtodo.data.DAO.TaskDao
import com.codinginflow.mvvmtodo.data.Task

class AddTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
    set(value) {
        field = value
        state.set("taskName", value)
    }
    var taskDescription = state.get<String>("taskDescription") ?: task?.desc ?: ""
        set(value) {
            field = value
            state.set("taskDescription", value)
        }
    var taskDate = state.get<String>("taskDate") ?: task?.date ?: "2000-01-01"
        set(value) {
            field = value
            state.set("taskDate", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.isImportant ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }
}