package com.example.budgetbonsai

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvWishlistName: TextView = itemView.findViewById(R.id.tv_wishlist_name)
    val tvDeadline: TextView = itemView.findViewById(R.id.tv_deadline)
    val depositButton: Button = itemView.findViewById(R.id.btn_deposit)
    val withdrawButton: Button = itemView.findViewById(R.id.btn_withdraw)

    fun setOnClickListener(listener: (Int) -> Unit) {
        depositButton.setOnClickListener { listener(R.id.btn_deposit) }
        withdrawButton.setOnClickListener { listener(R.id.btn_withdraw) }
    }

    fun bind(item: Wishlist) {
        tvWishlistName.text = item.name
        tvWishlistName.text = item.deadline
    }
}