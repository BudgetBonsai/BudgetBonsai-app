package com.example.budgetbonsai.ui.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.databinding.FragmentDailyBinding
import com.example.budgetbonsai.utils.Result
import com.example.budgetbonsai.repository.TransactionRepository
import com.example.budgetbonsai.data.local.dataStore

class DailyFragment : Fragment() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: TransactionAdapter
    private lateinit var userPreference: UserPreference
    private lateinit var binding: FragmentDailyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        val view = binding.root

        userPreference = UserPreference.getInstance(requireContext().dataStore)

        adapter = TransactionAdapter(emptyList())
        binding.recyclerViewDaily.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDaily.adapter = adapter

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
                    Toast.makeText(requireContext(), "There are no Transaction", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewModel.getTransactions()

        return view
    }
}