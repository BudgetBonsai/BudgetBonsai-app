package com.example.budgetbonsai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WishlistAdapter(private val wishlist: List<Wishlist>) : RecyclerView.Adapter<WishlistAdapter.HeroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wishlist, parent, false)
        return HeroViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val wishlist = wishlist[position]
        holder.tvWishlistName.text = wishlist.name
        holder.tvWishlistPrice.text = wishlist.price.toString()
        holder.tvWishlistSaving.text = wishlist.saving.toString()

    }

    override fun getItemCount(): Int {
        return wishlist.size
    }

    class HeroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWishlistName: TextView = itemView.findViewById(R.id.tv_wishlist_name)
        val tvWishlistPrice: TextView = itemView.findViewById(R.id.tv_wishlist_price)
        val tvWishlistSaving: TextView = itemView.findViewById(R.id.tv_wishlist_saving)
    }
}