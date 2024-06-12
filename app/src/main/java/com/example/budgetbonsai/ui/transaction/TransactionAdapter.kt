package com.example.budgetbonsai.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbonsai.R
import com.example.budgetbonsai.data.remote.response.DataItem

class TransactionAdapter(private var transactions: List<DataItem>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun setItems(transactions: List<DataItem>) {
        this.transactions = transactions.sortedByDescending { it.date?.seconds }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val historyDate: TextView = itemView.findViewById(R.id.history_date)
        private val historyName: TextView = itemView.findViewById(R.id.history_name)
        private val historyAmount: TextView = itemView.findViewById(R.id.history_amount)

        fun bind(transaction: DataItem) {
            val dateFormatted = transaction.date?.seconds?.let {
                java.text.SimpleDateFormat("dd MMM, HH.mm", java.util.Locale.getDefault()).format(java.util.Date(it.toLong() * 1000))
            } ?: "No Date"

            historyDate.text = dateFormatted
            historyName.text = transaction.name
//            historyAmount.text = "$${transaction.amount}"
            historyAmount.text = "${getTransactionSign(transaction.type)} ${transaction.amount}"
        }
    }

    private fun getTransactionSign(transactionType: String?): String {
        return when (transactionType) {
            "Income" -> "+"
            "Outcome" -> "-"
            else -> ""
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}