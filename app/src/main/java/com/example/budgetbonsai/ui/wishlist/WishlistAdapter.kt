package com.example.budgetbonsai.ui.wishlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbonsai.data.remote.response.WishlistItem
import com.example.budgetbonsai.databinding.ItemWishlistBinding

class WishlistAdapter(private val context: Context, private var wishlist: List<WishlistItem>) : RecyclerView.Adapter<WishlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val binding = ItemWishlistBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WishlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        holder.btnDeposit.setOnClickListener {
            showDepositDialog()
        }
        holder.btnWithdraw.setOnClickListener {
            showWithdrawDialog()
        }
        holder.bind(wishlist[position])
    }

    fun setItems(items: List<WishlistItem>) {
        wishlist = items
        notifyDataSetChanged()
    }

    private fun showDepositDialog() {
        val dialog = DepositFragment()
        if (context is FragmentActivity) {
            dialog.show((context as FragmentActivity).supportFragmentManager, DepositFragment.TAG)
        } else {
            throw ClassCastException("Context must be an instance of FragmentActivity")
        }
    }

    private fun showWithdrawDialog() {
        val dialog = WithdrawFragment()
        if (context is FragmentActivity) {
            dialog.show((context as FragmentActivity).supportFragmentManager, WithdrawFragment.TAG)
        } else {
            throw ClassCastException("Context must be an instance of FragmentActivity")
        }
    }

    override fun getItemCount(): Int {
        return wishlist.size
    }
}