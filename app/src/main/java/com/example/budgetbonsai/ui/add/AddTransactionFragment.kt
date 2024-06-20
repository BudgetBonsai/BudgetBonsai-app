package com.example.budgetbonsai.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.budgetbonsai.utils.Category
import com.example.budgetbonsai.R
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore
import com.example.budgetbonsai.data.model.Transaction
import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.data.remote.response.AddTransactionResponse
import com.example.budgetbonsai.databinding.FragmentAddTransactionBinding
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        binding.edtInputdate.setText(currentDate)

        val total = arguments?.getString("total")
        val name = arguments?.getString("name")

        binding.edtInputamount.setText(total)
        binding.edtInputname.setText(name)

        binding.edtInputdate.setOnClickListener {
            showDatePickerDialog()
        }

        userPreference = UserPreference.getInstance(requireContext().dataStore)

        binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_expense -> {
                        val listExpense = Category.expenseCategory()
                        val expensesAdapter = ArrayAdapter(requireContext(), R.layout.list_item, listExpense)
                        binding.autoCompleteTextView.setAdapter(expensesAdapter)
                    }
                    R.id.btn_income -> {
                        val listIncome = Category.incomeCategory()
                        val incomeAdapter = ArrayAdapter(requireContext(), R.layout.list_item, listIncome)
                        binding.autoCompleteTextView.setAdapter(incomeAdapter)
                    }
                }
            }
        }

        binding.btnAdd.setOnClickListener {
            val date = binding.edtInputdate.text.toString()
            val name = binding.edtInputname.text.toString()
            val amount = binding.edtInputamount.text.toString().toInt()
            val category = binding.autoCompleteTextView.text.toString()
            val type = when (binding.toggleButton.checkedButtonId) {
                R.id.btn_income -> "income"
                R.id.btn_expense -> "expense"
                else -> ""
            }
            val transaction = Transaction(date, name, amount, category, type)
            sendTransactionToApi(transaction)
        }

        val btnBack = binding.backImageButton
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth)
                binding.edtInputdate.setText(selectedDate)
            },

            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun sendTransactionToApi(transaction: Transaction) {
        lifecycleScope.launch {
            val token = userPreference.getSession().firstOrNull()?.token ?: ""
            if (token.isNotEmpty()) {
                val apiService = ApiConfig.getApiService(userPreference)
                val call = apiService.addTransaction(transaction)

                call.enqueue(object : Callback<AddTransactionResponse> {
                    override fun onResponse(call: Call<AddTransactionResponse>, response: Response<AddTransactionResponse>) {
                        if (response.isSuccessful) {
                            val transactionResponse = response.body()
                            if (transactionResponse != null && !transactionResponse.error!!) {
                                Toast.makeText(requireContext(), transactionResponse.message, Toast.LENGTH_SHORT).show()
                                Log.i("API Response", "Transaction added successfully: ${transactionResponse.data}")
                                parentFragmentManager.popBackStack()
                            } else {
                                Toast.makeText(requireContext(), "Failed to add transaction", Toast.LENGTH_SHORT).show()
                                Log.e("API Response", "Error: ${response.errorBody()?.string()}")
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to add transaction", Toast.LENGTH_SHORT).show()
                            Log.e("API Response", "Response Code: ${response.code()}, Response Message: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<AddTransactionResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("API Failure", t.message ?: "Unknown error")
                    }
                })
            } else {
                Toast.makeText(requireContext(), "User token is empty", Toast.LENGTH_SHORT).show()
                Log.e("Token Error", "User token is empty")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}