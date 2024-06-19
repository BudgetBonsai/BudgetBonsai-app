    package com.example.budgetbonsai.ui.home

    import android.content.Context
    import android.content.SharedPreferences
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
    import com.example.budgetbonsai.data.remote.PredictApiConfig
    import com.example.budgetbonsai.data.remote.PredictApiService
    import com.example.budgetbonsai.databinding.FragmentHomeBinding
    import com.example.budgetbonsai.databinding.FragmentWishlistBinding
    import com.example.budgetbonsai.repository.HomeRepository
    import com.example.budgetbonsai.utils.Result
    import com.example.budgetbonsai.repository.Repository
    import com.example.budgetbonsai.repository.WishlistRepository
    import com.example.budgetbonsai.ui.wishlist.WishlistViewModel
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
        private lateinit var viewModel: HomeViewModel

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            val view = binding.root

            userPreference = UserPreference.getInstance(requireContext().dataStore)
            apiService = PredictApiConfig.getApiService(userPreference) // inisialisasi apiService sebelum menggunakannya

            val repository = HomeRepository(apiService, userPreference)
            viewModel = ViewModelProvider(this, HomeViewModel.HomeViewModelFactory(repository)).get(HomeViewModel::class.java)

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

//        private fun observeGrowthMessage() {
//            viewModel.growthMessageLiveData.observe(viewLifecycleOwner) { result ->
//                when (result) {
//                    is Result.Loading -> {
//                        // Tampilkan loading
//                        // Misalnya, Anda bisa menampilkan progress bar
//                        // binding.progressBar.visibility = View.VISIBLE
//                    }
//                    is Result.Success -> {
//                        // Sembunyikan loading
//                        // binding.progressBar.visibility = View.GONE
//                        // Tampilkan hasil growth message ke TextView
//                        val growthResponse = result.data
//                        val message = growthResponse.growthMessage
//                        tvPrediction.text = message
//                    }
//                    is Result.Error -> {
//                        // Sembunyikan loading
//                        // binding.progressBar.visibility = View.GONE
//                        // Tampilkan pesan error
//                        Toast.makeText(context, "Error fetching growth message", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
    }