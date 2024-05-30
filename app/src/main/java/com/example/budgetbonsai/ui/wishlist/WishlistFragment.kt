package com.example.budgetbonsai.ui.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbonsai.R
import com.example.budgetbonsai.Wishlist
import com.example.budgetbonsai.WishlistAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WishlistFragment : Fragment() {

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

        recyclerView = view.findViewById(R.id.rv_heroes)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        // Prepare data
        wishlist = listOf(
            Wishlist("Laptop", 1000.0, 100.0),
            Wishlist("Phone", 500.0, 50.0),
            Wishlist("Tablet", 300.0, 30.0),
            Wishlist("Smartwatch", 200.0, 20.0),
            Wishlist("Headphones", 100.0, 10.0)
        )

        // Set adapter
        wishlistAdapter = WishlistAdapter(wishlist)
        recyclerView?.adapter = wishlistAdapter

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            // Use NavController to navigate to the other fragment
            findNavController().navigate(R.id.action_wishlistFragment_to_addFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null // Avoid memory leaks
    }
}
