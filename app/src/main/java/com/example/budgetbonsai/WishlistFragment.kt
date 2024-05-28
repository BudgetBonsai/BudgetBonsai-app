package com.example.budgetbonsai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null // Avoid memory leaks
    }
}
