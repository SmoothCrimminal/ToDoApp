package com.codinginflow.mvvmtodo.ui.addtask

import android.widget.DatePicker
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmtodo.data.DAO.TaskDao
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.ui.ADD_TASK_RESULT_OK
import com.codinginflow.mvvmtodo.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

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
    var taskDate = state.get<String>("taskDate") ?: task?.date ?: "2021-05-13"
        set(value) {
            field = value
            state.set("taskDate", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.isImportant ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }

    private val addTaskEventChannel = Channel<AddTaskEvent>()
    val addTaskEvent = addTaskEventChannel.receiveAsFlow()

    fun onSaveClick(){
        if (taskName.isBlank() || taskDescription.isBlank()) {
            showInvalidInputMessage("Please fill in description or name first!")
            return
        }

        if (task != null) {
            val updatedTask = task.copy(
                name = taskName,
                isImportant = taskImportance,
                date = taskDate,
                desc = taskDescription
            )
            updateTask(updatedTask)
        } else {
            val newTask = Task(
                name = taskName,
                isImportant = taskImportance,
                date = taskDate,
                desc = taskDescription
            )
            createTask(newTask)
        }
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.Insert(task)
        addTaskEventChannel.send(AddTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.Update(task)
        addTaskEventChannel.send(AddTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addTaskEventChannel.send(AddTaskEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddTaskEvent{
        data class ShowInvalidInputMessage(val msg: String) : AddTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddTaskEvent()
    }
}