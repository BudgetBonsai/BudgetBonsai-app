package com.example.budgetbonsai.ui.transaction

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetbonsai.R
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore
import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.utils.Result
import com.example.budgetbonsai.repository.TransactionRepository
import com.example.budgetbonsai.data.remote.response.DataItem
import com.example.budgetbonsai.databinding.FragmentTransactionBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.tabs.TabLayoutMediator

class TransactionFragment : Fragment() {

    private lateinit var binding: FragmentTransactionBinding
    private lateinit var viewModel: TransactionViewModel
    private lateinit var pieChart: PieChart
    private lateinit var adapter: TransactionAdapter
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        adapter = TransactionAdapter(emptyList())
        binding.rvTransaction.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransaction.adapter = adapter

        val apiService = ApiConfig.getApiService(userPreference)
        val repository = TransactionRepository(apiService, userPreference)
        viewModel = ViewModelProvider(this, TransactionViewModelFactory(repository)).get(TransactionViewModel::class.java)

        viewModel.transactionsLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.setItems(result.data)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.totals.observe(viewLifecycleOwner) { (income, expense) ->
            binding.tvIncomeAmount.text = "$income"
            binding.tvOutcomeAmount.text = "$expense"
        }

        viewModel.getTransactions()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        pieChart = view.findViewById(R.id.piechart)

        viewModel.transactionsLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    updatePieChart(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Pie Chart Load Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updatePieChart(transactions: List<DataItem>) {
        val categoryTotals = mutableMapOf<String, Float>()

        transactions.forEach { transaction ->
            val amount = transaction.amount?.toFloat() ?: 0f
            val category = transaction.category ?: "Others"
            categoryTotals[category] = categoryTotals.getOrDefault(category, 0f) + amount
        }

        val pieEntries = categoryTotals.map { PieEntry(it.value, it.key) }
        val pieDataset = PieDataSet(pieEntries, "")
        pieDataset.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        pieDataset.valueTextSize = 15f
        pieDataset.valueTextColor = Color.BLACK

        val pieData = PieData(pieDataset)
        pieChart.data = pieData

        pieChart.description.isEnabled = false

        pieChart.animateY(1000)
    }
}