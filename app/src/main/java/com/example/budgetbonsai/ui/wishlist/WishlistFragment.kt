package com.example.budgetbonsai.ui.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.budgetbonsai.R
import com.example.budgetbonsai.ViewModelFactory
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore
import com.example.budgetbonsai.data.model.Wishlist
import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.databinding.FragmentWishlistBinding
import com.example.budgetbonsai.repository.TransactionRepository
import com.example.budgetbonsai.repository.WishlistRepository
import com.example.budgetbonsai.ui.transaction.TransactionAdapter
import com.example.budgetbonsai.ui.transaction.TransactionViewModel
import com.example.budgetbonsai.ui.transaction.TransactionViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private var recyclerView: RecyclerView? = null
    private lateinit var viewModel: WishlistViewModel
    private lateinit var userPreference: UserPreference
    private lateinit var adapter: WishlistAdapter
    private lateinit var wishlist: List<Wishlist>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        val view = binding.root

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        adapter = WishlistAdapter(requireContext(), emptyList())
        binding.rvWishlist.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWishlist.adapter = adapter

        val apiService = ApiConfig.getApiService(userPreference)
        val repository = WishlistRepository(apiService)
        viewModel = ViewModelProvider(this, WishlistViewModel.WishlistViewModelFactory(repository)).get(WishlistViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        recyclerView = view.findViewById(R.id.rv_wishlist)
        recyclerView = binding.rvWishlist
        recyclerView?.layoutManager = LinearLayoutManager(context)

        // Set adapter
//        wishlistAdapter = WishlistAdapter(requireContext(), wishlist)
//        recyclerView?.adapter = wishlistAdapter

        viewModel.wishlist.observe(viewLifecycleOwner, Observer { wishlist ->
            if (wishlist.isNotEmpty()) {
                val adapter = WishlistAdapter(requireContext(), wishlist)
                binding.rvWishlist.adapter = adapter
            }
        })

        viewModel.fetchWishlist()

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            // Use NavController to navigate to the other fragment
            findNavController().navigate(R.id.action_wishlistFragment_to_addFragment)
        }
    }

//    override fun onDepositClick(item: Wishlist) {val fragment = DepositFragment()
//        val bundle = Bundle()
//        bundle.putString("wishlist_name", item.name)
//        fragment.arguments = bundle
//
//        parentFragmentManager.beginTransaction()
//            .add(R.id.nav_host_fragment, fragment)
//            .addToBackStack(null)
//            .commit()
//    }
//
//    override fun onWithdrawClick(item: Wishlist) {
//        val fragment = WithdrawFragment()
//        val bundle = Bundle()
//        bundle.putString("wishlist_name", item.name)
//        fragment.arguments = bundle
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.nav_host_fragment, fragment)
//            .addToBackStack(null)
//            .commit()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
    }
}
