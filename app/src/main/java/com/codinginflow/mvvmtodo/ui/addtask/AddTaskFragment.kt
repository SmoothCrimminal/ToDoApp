package com.codinginflow.mvvmtodo.ui.addtask

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.AddTaskFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_task_fragment.view.*
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
        val startDate = viewModel.taskDate
        val date = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE)

        binding.apply {
            editTextTaskName.setText(viewModel.taskName)
            editTextTaskDescription.setText(viewModel.taskDescription)
            checkboxImportant.isChecked = viewModel.taskImportance
            checkboxImportant.jumpDrawablesToCurrentState()
            datePicker.date_picker.updateDate(date.year, date.monthValue, date.dayOfMonth)
        }
    }
}
