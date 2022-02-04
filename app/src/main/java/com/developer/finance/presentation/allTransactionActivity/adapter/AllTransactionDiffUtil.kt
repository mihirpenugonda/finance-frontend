package com.developer.finance.presentation.expensesFragment.adapter

import androidx.recyclerview.widget.DiffUtil
import com.developer.finance.data.local.entity.Transaction

class AllTransactionDiffUtil(
    private val oldList: List<Transaction>,
    private val newList: List<Transaction>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
