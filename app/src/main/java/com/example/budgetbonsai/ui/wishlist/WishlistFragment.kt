package com.example.budgetbonsai.ui.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbonsai.DepositFragment
import com.example.budgetbonsai.R
import androidx.lifecycle.Observer
import com.example.budgetbonsai.ViewModelFactory
import com.example.budgetbonsai.Wishlist
import com.example.budgetbonsai.WishlistAdapter
import com.example.budgetbonsai.WishlistViewModelFactory
import com.example.budgetbonsai.WithdrawFragment
import com.example.budgetbonsai.data.Repository
import com.example.budgetbonsai.data.WishlistRepository
import com.example.budgetbonsai.data.local.room.WishlistDao
import com.example.budgetbonsai.data.local.room.WishlistDatabase
import com.example.budgetbonsai.data.model.WishlistTest
import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.ui.WishlistAdapterTest
import com.example.budgetbonsai.ui.settings.SettingsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WishlistFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private lateinit var wishlistAdapter: WishlistAdapterTest
    private lateinit var viewModel: WishlistViewModel
//    private lateinit var wishlist: List<Wishlist>
    private lateinit var wishlist: List<WishlistTest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val applicationContext = requireActivity().applicationContext
        val dao = WishlistDatabase.getDatabase(applicationContext).wishlistDao()
        val repository = WishlistRepository(dao)

        val viewModelFactory = WishlistViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WishlistViewModel::class.java)

        recyclerView = view.findViewById(R.id.rv_wishlist)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        viewModel.allWishlist.observe(viewLifecycleOwner, Observer { wishlists ->
            wishlistAdapter = WishlistAdapterTest(
                requireContext(),
                wishlists,
                onDepositClick = { item ->
                    val depositFragment = DepositFragment()
                    depositFragment.show(parentFragmentManager, "DepositFragment")
                },
                onWithdrawClick = { item ->
                    val withdrawFragment = WithdrawFragment()
                    withdrawFragment.show(parentFragmentManager, "WithdrawFragment")
                },
                onEditClick = { item ->
                    // Implementasi saat item diedit
                },
                onDeleteClick = { item ->
                    // Implementasi saat item dihapus
                }
            )
            recyclerView?.adapter = wishlistAdapter
        })

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            // Use NavController to navigate to the other fragment
            findNavController().navigate(R.id.action_wishlistFragment_to_addFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
    }
}
