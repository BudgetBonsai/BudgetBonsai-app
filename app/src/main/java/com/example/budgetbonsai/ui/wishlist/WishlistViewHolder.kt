package com.example.budgetbonsai.ui.wishlist

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.budgetbonsai.R
import com.example.budgetbonsai.data.model.Wishlist
import com.example.budgetbonsai.data.remote.response.WishlistItem
import com.example.budgetbonsai.databinding.ItemWishlistBinding

class WishlistViewHolder(private val binding: ItemWishlistBinding) : RecyclerView.ViewHolder(binding.root) {

    val btnDeposit: Button = binding.btnDeposit
    val btnWithdraw: Button = binding.btnWithdraw

    fun setOnClickListener(listener: (Int) -> Unit) {
        binding.btnDeposit.setOnClickListener { listener(R.id.btn_deposit) }
        binding.btnWithdraw.setOnClickListener { listener(R.id.btn_withdraw) }
    }

    fun bind(item: WishlistItem) {
        Glide.with(binding.root.context)
            .load(item.image)
            .into(binding.ivWishlist)
        binding.tvWishlistName.text = item.name
        binding.tvWishlistLeft.text = "You currently saved $0.00 out of $${item.amount}.0"
        binding.tvDeadline.text = "You have until ${item.savingPlan} to finish this wishlist"
    }
}