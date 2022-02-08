package com.developer.finance.featureExpenseManagement.presentation.allTransactionActivity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.developer.finance.R
import com.developer.finance.common.DateTimeConverter
import com.developer.finance.databinding.RvExpenseItemBinding
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction

class AllTransactionAdapter : RecyclerView.Adapter<AllTransactionAdapter.TransactionViewHolder>() {

    var expenseList: List<Transaction>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    inner class TransactionViewHolder(val binding: RvExpenseItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllTransactionAdapter.TransactionViewHolder {
        return TransactionViewHolder(
            RvExpenseItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: AllTransactionAdapter.TransactionViewHolder,
        position: Int
    ) {
        val expense = expenseList[position]
        with(holder.binding) {
            expenseName.text = expense.title
            expenseDate.text = DateTimeConverter().format(expense.date)
            expenseCategory.text = expense.category
            expenseAmount.text = expense.amount

            if (expense.type == "income") {
                val amount = expense.amount
                expenseAmount.text = "+₹$amount"
                expenseAmount.setTextColor(
                    ContextCompat.getColor(
                        expenseAmount.context,
                        R.color.income
                    )
                )
            } else {
                val amount = expense.amount
                expenseAmount.text = "-₹$amount"
                expenseAmount.setTextColor(
                    ContextCompat.getColor(
                        expenseAmount.context,
                        R.color.expense
                    )
                )
            }

            when (expense.category) {
                "housing" -> {
                    categoryImage.setImageResource(R.drawable.ic_food)
                }
                "transportation" -> {
                    categoryImage.setImageResource(R.drawable.ic_transport)
                }
                "food" -> {
                    categoryImage.setImageResource(R.drawable.ic_food)
                }
                "utilities" -> {
                    categoryImage.setImageResource(R.drawable.ic_utilities)
                }
                "insurance" -> {
                    categoryImage.setImageResource(R.drawable.ic_insurance)
                }
                "healthcare" -> {
                    categoryImage.setImageResource(R.drawable.ic_medical)
                }
                "saving & debts" -> {
                    categoryImage.setImageResource(R.drawable.ic_savings)
                }
                "personal spending" -> {
                    categoryImage.setImageResource(R.drawable.ic_personal_spending)
                }
                "entertainment" -> {
                    categoryImage.setImageResource(R.drawable.ic_entertainment)
                }
                "miscellaneous" -> {
                    categoryImage.setImageResource(R.drawable.ic_others)
                }
                else -> {
                    categoryImage.setImageResource(R.drawable.ic_others)
                }
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(expense) }
        }
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    private var onItemClickListener: ((Transaction) -> Unit)? = null
    fun setOnItemClickListener(listener: (Transaction) -> Unit) {
        onItemClickListener = listener
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)

}