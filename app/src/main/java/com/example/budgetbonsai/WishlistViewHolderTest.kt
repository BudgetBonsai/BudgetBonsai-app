package com.example.budgetbonsai

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbonsai.data.model.WishlistTest

class WishlistViewHolderTest(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvWishlistName: TextView = itemView.findViewById(R.id.tv_wishlist_name)
    val tvDeadline: TextView = itemView.findViewById(R.id.tv_deadline)
    val tvAmount: TextView = itemView.findViewById(R.id.tv_wishlist_left)
    val btnDeposit: Button = itemView.findViewById(R.id.btn_deposit)
    val btnWithdraw: Button = itemView.findViewById(R.id.btn_withdraw)
    val btnEdit: ImageButton = itemView.findViewById(R.id.btn_edit_wishlist)
    val btnDelete: ImageButton = itemView.findViewById(R.id.btn_del_wishlist)

    fun bind(
        item: WishlistTest,
        onDepositClick: (WishlistTest) -> Unit,
        onWithdrawClick: (WishlistTest) -> Unit,
        onEditClick: (WishlistTest) -> Unit,
        onDeleteClick: (WishlistTest) -> Unit
    ) {
        tvWishlistName.text = item.name
        tvDeadline.text = item.deadline

        val amountText = "You currently saved $%.2f out of $%.2f".format(item.currentAmount, item.targetAmount)
        tvAmount.text = amountText

        btnDeposit.setOnClickListener { onDepositClick(item) }
        btnWithdraw.setOnClickListener { onWithdrawClick(item) }
        btnEdit.setOnClickListener { onEditClick(item) }
        btnDelete.setOnClickListener { onDeleteClick(item) }
    }
}