package com.codinginflow.mvvmtodo.ui.tasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.databinding.ItemTaskBinding


class TaskAdapter : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(task: Task){

            binding.apply {
                checkboxCompleted.isChecked = task.isCompleted
                textviewName.text = task.name
                textviewName.paint.isStrikeThruText = task.isCompleted
                labelPriority.isVisible = task.isImportant
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>(){

        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    }
}