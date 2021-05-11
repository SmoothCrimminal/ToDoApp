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
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.TasksFragmentBinding
import com.codinginflow.mvvmtodo.ui.tasks.adapters.TaskAdapter
import com.codinginflow.mvvmtodo.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.tasks_fragment) {
    private val viewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = TasksFragmentBinding.bind(view)

        val taskAdapter = TaskAdapter()

        binding.apply {
            recycleViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner) {

            taskAdapter.submitList(it)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_fragment, menu)

        val searchItem = menu.findItem(R.id.search_item)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
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
                true
                //TODO
            }

           R.id.sort_by_todo_date -> {
               true
               // TODO
           }

           R.id.hide_completed -> {

               item.isChecked = !item.isChecked
               true
               // TODO
           }

           R.id.delete_completed -> {
               true
               // TODO
           }

           else -> super.onOptionsItemSelected(item)
        }
    }
}