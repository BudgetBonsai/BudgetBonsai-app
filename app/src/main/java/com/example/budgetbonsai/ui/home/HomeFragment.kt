    package com.example.budgetbonsai.ui.home

    import android.content.Context
    import android.content.SharedPreferences
    import android.graphics.Color
    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.TextView
    import android.widget.Toast
    import androidx.datastore.core.DataStore
    import androidx.datastore.preferences.core.Preferences
    import androidx.datastore.preferences.preferencesDataStore
    import androidx.fragment.app.Fragment
    import androidx.lifecycle.ViewModelProvider
    import androidx.lifecycle.lifecycleScope
    import com.example.budgetbonsai.R
    import com.example.budgetbonsai.ViewModelFactory
    import com.example.budgetbonsai.data.local.UserPreference
    import com.example.budgetbonsai.data.local.dataStore
    import com.example.budgetbonsai.data.remote.ApiConfig
    import com.example.budgetbonsai.data.remote.ApiService
    import com.example.budgetbonsai.data.remote.PredictApiConfig
    import com.example.budgetbonsai.data.remote.PredictApiService
    import com.example.budgetbonsai.data.remote.response.DataItem
    import com.example.budgetbonsai.databinding.FragmentHomeBinding
    import com.example.budgetbonsai.databinding.FragmentWishlistBinding
    import com.example.budgetbonsai.repository.HomeRepository
    import com.example.budgetbonsai.utils.Result
    import com.example.budgetbonsai.repository.Repository
    import com.example.budgetbonsai.repository.TransactionRepository
    import com.example.budgetbonsai.repository.WishlistRepository
    import com.example.budgetbonsai.ui.transaction.TransactionViewModel
    import com.example.budgetbonsai.ui.transaction.TransactionViewModelFactory
    import com.example.budgetbonsai.ui.wishlist.WishlistViewModel
    import com.github.mikephil.charting.charts.PieChart
    import com.github.mikephil.charting.data.PieData
    import com.github.mikephil.charting.data.PieDataSet
    import com.github.mikephil.charting.data.PieEntry
    import com.github.mikephil.charting.utils.ColorTemplate
    import kotlinx.coroutines.flow.firstOrNull
    import kotlinx.coroutines.launch

    /**
     * A simple [Fragment] subclass.
     * Use the [HomeFragment.newInstance] factory method to
     * create an instance of this fragment.
     */
    class HomeFragment : Fragment() {
        private var _binding: FragmentHomeBinding? = null
        private val binding get() = _binding!!
        private lateinit var userPreference: UserPreference
        private lateinit var apiService: PredictApiService
        private lateinit var transactionApiService: ApiService
        private lateinit var viewModel: HomeViewModel
        private lateinit var transactionViewModel: TransactionViewModel
        private lateinit var pieChart: PieChart

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            val view = binding.root

            userPreference = UserPreference.getInstance(requireContext().dataStore)
            apiService = PredictApiConfig.getApiService(userPreference)
            transactionApiService = ApiConfig.getApiService(userPreference)

            val repository = HomeRepository(apiService, userPreference)
            viewModel = ViewModelProvider(this, HomeViewModel.HomeViewModelFactory(repository)).get(HomeViewModel::class.java)

            val transactionRepository = TransactionRepository(transactionApiService, userPreference)
            transactionViewModel = ViewModelProvider(this, TransactionViewModelFactory(transactionRepository)).get(TransactionViewModel::class.java)

            lifecycleScope.launch {
                userPreference.getSession().collect { user ->
                    binding.tvName.text = user.name
                }
            }

            viewModel.predictLiveData.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val growthResponse = result.data
                        val message = growthResponse.growthMessage
                        binding.tvPrediction.text = message
                        setImageBasedOnPrediction(growthResponse.prediction)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.e("HomeFragment", "Error fetching growth message: ${result.error}")
                    }
                }
            }

            transactionViewModel.transactionsLiveData.observe(viewLifecycleOwner) { result ->
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

            pieChart = view.findViewById(R.id.piechart)

            transactionViewModel.getTransactions()

            return view
        }

        private fun setImageBasedOnPrediction(prediction: Any?) {
            if (prediction is Number) {
                if (prediction.toDouble() >= 0) {
                    binding.imageView.setImageResource(R.drawable.bonsai_happy)
                } else {
                    binding.imageView.setImageResource(R.drawable.bonsai_sad)
                }
            } else {
                binding.imageView.setImageResource(R.drawable.bonsai_neutral)
            }
        }

        private fun observeGrowthMessage() {
            viewModel.growthMessageLiveData.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val growthResponse = result.data
                        val message = growthResponse.growthMessage
                        binding.tvPrediction.text = message
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.e("HomeFragment", "Error fetching growth message: ${result.error}")
                        Toast.makeText(context, "Error fetching growth message", Toast.LENGTH_SHORT).show()
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
            pieData.setValueTextSize(0f)
            pieChart.data = pieData
            pieChart.setDrawEntryLabels(false)

            pieChart.description.isEnabled = false

            pieChart.animateY(1000)
        }

        override fun onResume() {
            super.onResume()
            transactionViewModel.getTransactions()
            viewModel.fetchPredict()
        }
    }