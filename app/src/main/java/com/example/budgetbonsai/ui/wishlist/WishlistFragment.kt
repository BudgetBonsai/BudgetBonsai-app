package com.example.budgetbonsai.ui.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbonsai.R
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore

import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.data.remote.response.WishlistItem
import com.example.budgetbonsai.databinding.FragmentWishlistBinding
import com.example.budgetbonsai.repository.TransactionRepository
import com.example.budgetbonsai.repository.WishlistRepository
import com.example.budgetbonsai.ui.transaction.TransactionAdapter
import com.example.budgetbonsai.ui.transaction.TransactionViewModel
import com.example.budgetbonsai.ui.transaction.TransactionViewModelFactory
import com.example.budgetbonsai.utils.Result
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private var recyclerView: RecyclerView? = null
    private lateinit var viewModel: WishlistViewModel
    private lateinit var userPreference: UserPreference
    private lateinit var adapter: WishlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        val view = binding.root

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        adapter = WishlistAdapter(requireContext(), emptyList())
        binding.rvWishlist.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWishlist.adapter = adapter

        val apiService = ApiConfig.getApiService(userPreference)
        val repository = WishlistRepository(apiService)
        viewModel = ViewModelProvider(this, WishlistViewModel.WishlistViewModelFactory(repository, requireContext())).get(WishlistViewModel::class.java)

        viewModel.wishlistLiveData.observe(viewLifecycleOwner) { result ->
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
                    Toast.makeText(context, "No Wishlist Available", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvWishlist
        recyclerView?.layoutManager = LinearLayoutManager(context)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_wishlistFragment_to_addFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchWishlist()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
    }
}
