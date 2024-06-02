package com.example.budgetbonsai.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.budgetbonsai.Category
import com.example.budgetbonsai.R
import com.example.budgetbonsai.databinding.FragmentAddExpensesBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddExpensesFragment : Fragment() {

    private var _binding: FragmentAddExpensesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        binding.dateInputEditText.setText(currentDate)

        binding.dateInputEditText.setOnClickListener {
            showDatePickerDialog()
        }

        binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_expense -> {
                        val listExpense = Category.expenseCategory()
                        val expensesAdapter = ArrayAdapter(requireContext(),
                            R.layout.list_item, listExpense)
                        binding.autoCompleteTextView.setAdapter(expensesAdapter)
                    }
                    R.id.btn_income -> {
                        val listIncome = Category.incomeCategory()
                        val incomeAdapter = ArrayAdapter(requireContext(),
                            R.layout.list_item, listIncome)
                        binding.autoCompleteTextView.setAdapter(incomeAdapter)
                    }
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth)
                binding.dateInputEditText.setText(selectedDate)
            },
            // testing
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

}