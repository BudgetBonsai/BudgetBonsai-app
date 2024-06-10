package com.example.budgetbonsai.ui.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbonsai.DepositFragment
import com.example.budgetbonsai.R
import com.example.budgetbonsai.Wishlist
import com.example.budgetbonsai.WishlistAdapter
import com.example.budgetbonsai.WithdrawFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WishlistFragment : Fragment(), WishlistAdapter.OnItemClickListener {

    private var recyclerView: RecyclerView? = null
    private lateinit var wishlistAdapter: WishlistAdapter
    private lateinit var wishlist: List<Wishlist>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_wishlist)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        // Prepare data
        wishlist = listOf(
            Wishlist("Laptop", 1000.0, 100.0, "2021-12-31"),
            Wishlist("Smartphone", 500.0, 50.0, "2021-12-31"),
            Wishlist("Smartwatch", 300.0, 30.0, "2021-12-31"),
            Wishlist("Tablet", 200.0, 20.0, "2021-12-31"),
            Wishlist("Headphone", 100.0, 10.0, "2021-12-31")
        )

        // Set adapter
        wishlistAdapter = WishlistAdapter(wishlist, this)
        recyclerView?.adapter = wishlistAdapter

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            // Use NavController to navigate to the other fragment
            findNavController().navigate(R.id.action_wishlistFragment_to_addFragment)
        }
    }

    override fun onDepositClick(item: Wishlist) {val fragment = DepositFragment()
        val bundle = Bundle()
        bundle.putString("wishlist_name", item.name)
        fragment.arguments = bundle

        // Menggunakan add() untuk menambahkan DepositFragment ke dalam back stack
        parentFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onWithdrawClick(item: Wishlist) {
        val fragment = WithdrawFragment()
        val bundle = Bundle()
        bundle.putString("wishlist_name", item.name)
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null // Avoid memory leaks
    }
}
