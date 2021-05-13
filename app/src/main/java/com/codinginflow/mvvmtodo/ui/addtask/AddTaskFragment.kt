package com.codinginflow.mvvmtodo.ui.addtask

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.AddTaskFragmentBinding
import com.codinginflow.mvvmtodo.utils.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_task_fragment.view.*
import kotlinx.coroutines.flow.collect
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class AddTaskFragment : Fragment(R.layout.add_task_fragment) {

    private val viewModel: AddTaskViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = AddTaskFragmentBinding.bind(view)
        var startDate = viewModel.taskDate
        val dateChanger = startDate[6].toInt() + 1
        val sb = StringBuilder(startDate).also { it.setCharAt(6, dateChanger.toChar()) }
        startDate = sb.toString()
        val date = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE)

        binding.apply {
            editTextTaskName.setText(viewModel.taskName)
            editTextTaskDescription.setText(viewModel.taskDescription)
            checkboxImportant.isChecked = viewModel.taskImportance
            checkboxImportant.jumpDrawablesToCurrentState()
            datePicker.date_picker.updateDate(date.year, date.monthValue - 1, date.dayOfMonth)

            editTextTaskName.addTextChangedListener{
                viewModel.taskName = it.toString()
            }

            editTextTaskDescription.addTextChangedListener {
                viewModel.taskDescription = it.toString()
            }

            checkboxImportant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportance = isChecked
            }

            datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                var month = (monthOfYear + 1).toString()
                var day = dayOfMonth.toString()
                if (monthOfYear < 10) {
                    month = "0$monthOfYear"
                }

                if (dayOfMonth < 10){
                    day = "0$dayOfMonth"
                }
                viewModel.taskDate = "$year-$month-$day"
            }

            fabSaveTask.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addTaskEvent.collect { event ->
                when (event) {
                    is AddTaskViewModel.AddTaskEvent.NavigateBackWithResult -> {
                        binding.editTextTaskName.clearFocus()
                        binding.editTextTaskDescription.clearFocus()
                        setFragmentResult(
                            "add_task_request",
                            bundleOf("add_task_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddTaskViewModel.AddTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
    }
}
