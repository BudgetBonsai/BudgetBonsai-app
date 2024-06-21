package com.example.budgetbonsai.ui.wishlist

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.budgetbonsai.R
import com.example.budgetbonsai.data.remote.response.WishlistItem
import com.example.budgetbonsai.databinding.ItemWishlistBinding

class WishlistViewHolder(private val binding: ItemWishlistBinding) : RecyclerView.ViewHolder(binding.root) {

    val btnDelWishlist: ImageButton = binding.btnDelWishlist
    val btnEditWishlist: ImageButton = binding.btnEditWishlist
    val btnDeposit: Button = binding.btnDeposit

    fun setOnClickListener(listener: (Int) -> Unit) {
        binding.btnDeposit.setOnClickListener { listener(R.id.btn_deposit) }
    }

    fun bind(item: WishlistItem) {
        Glide.with(binding.root.context)
            .load(item.image)
            .into(binding.ivWishlist)
        binding.tvWishlistName.text = item.name
        if (item.amount == item.savingPlan?.toInt()) {
            binding.tvWishlistLeft.text = "Congratulations! You've reached your saving goal of $${item.savingPlan}!"
        } else {
            binding.tvWishlistLeft.text = "You currently saved $${item.amount} out of $${item.savingPlan}"
        }
    }
}