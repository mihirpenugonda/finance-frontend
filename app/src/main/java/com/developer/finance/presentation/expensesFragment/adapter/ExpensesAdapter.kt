package com.developer.finance.presentation.expensesFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.developer.finance.R
import com.developer.finance.data.local.entity.Expense
import com.developer.finance.databinding.RvExpenseItemBinding

class ExpensesAdapter : RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder>() {

    private var oldExpenseList: List<Expense> = emptyList()

    inner class ExpenseViewHolder(val binding: RvExpenseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        return ExpenseViewHolder(
            RvExpenseItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = oldExpenseList[position]
        with(holder.binding) {
            expenseName.text = expense.title
            expenseDate.text = expense.date
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
            onItemClickListener?.let {it(expense)}
        }
    }

    override fun getItemCount(): Int {
        return oldExpenseList.size
    }

    private var onItemClickListener: ((Expense) -> Unit)? = null
    fun setOnItemClickListener(listener: (Expense) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(newExpenseList: List<Expense>) {
        val diffUtil = ExpensesDiffUtil(oldExpenseList, newExpenseList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldExpenseList = newExpenseList
        diffResults.dispatchUpdatesTo(this)
    }
}