package com.codinginflow.mvvmtodo.ui.tasks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.data.PreferencesManager
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.databinding.TasksFragmentBinding
import com.codinginflow.mvvmtodo.ui.tasks.adapters.TaskAdapter
import com.codinginflow.mvvmtodo.utils.exhaustive
import com.codinginflow.mvvmtodo.utils.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.tasks_fragment), TaskAdapter.OnItemClickListener {
    private val viewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = TasksFragmentBinding.bind(view)

        val taskAdapter = TaskAdapter(this)

        binding.apply {
            recycleViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(recycleViewTasks)

            fabAddTask.setOnClickListener{
                viewModel.onAddNewTaskClick()
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.taskEvent.collect{ event ->
                when (event) {
                    is TasksViewModel.TaskEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                    is TasksViewModel.TaskEvent.NavigateToAddTaskScreen -> {
                        val action = TaskFragmentDirections.actionTaskFragmentToAddTaskFragment(null, "New Task")
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TaskEvent.NavigateToEditTaskScreen -> {
                        val action = TaskFragmentDirections.actionTaskFragmentToAddTaskFragment(event.task, "Edit Task")
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_fragment, menu)

        val searchItem = menu.findItem(R.id.search_item)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.hide_completed).isChecked =
                viewModel.preferencesFlow.first().hideCompleted
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when (item.itemId){
            R.id.github_redirect -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SmoothCrimminal"))
                startActivity(intent)
                true
            }

            R.id.sort_by_name -> {

                viewModel.onSortOrderSelected(PreferencesManager.SortOrder.BY_NAME)
                true
            }

           R.id.sort_by_todo_date -> {

               viewModel.onSortOrderSelected(PreferencesManager.SortOrder.BY_DATE)
               true
           }

           R.id.hide_completed -> {

               item.isChecked = !item.isChecked
               viewModel.onHideCompletedClick(item.isChecked)
               true
           }

           R.id.delete_completed -> {
               true
               // TODO
           }

           else -> super.onOptionsItemSelected(item)
        }
    }
}