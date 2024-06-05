package com.example.budgetbonsai.ui.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbonsai.R
import com.example.budgetbonsai.TransactionAdapter
import com.example.budgetbonsai.ViewPagerAdapter
import com.example.budgetbonsai.data.Repository
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.databinding.FragmentDailyBinding
import com.example.budgetbonsai.Result
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

        // Inisialisasi UserPreference
        userPreference = UserPreference.getInstance(requireContext().dataStore)

        // Inisialisasi RecyclerView dan adapter
        adapter = TransactionAdapter(emptyList())
        binding.recyclerViewDaily.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDaily.adapter = adapter

        // Inisialisasi ViewModel
        val apiService = ApiConfig.getApiService(userPreference)
        val repository = TransactionRepository(apiService, userPreference)
        viewModel = ViewModelProvider(this, TransactionViewModelFactory(repository)).get(TransactionViewModel::class.java)

        // Observasi LiveData dari ViewModel
        viewModel.transactionsLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Loading -> {
                    // Tampilkan indikator loading jika diperlukan
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.setItems(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    // Tampilkan pesan kesalahan kepada pengguna
                    Toast.makeText(requireContext(), "error min", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // Panggil fungsi untuk mendapatkan transaksi dari ViewModel
        viewModel.getTransactions()

        return view
    }
}