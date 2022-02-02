package com.developer.finance.presentation.expensesFragment

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
                "Housing" -> {
                    categoryImage.setImageResource(R.drawable.ic_food)
                }
                "Transportation" -> {
                    categoryImage.setImageResource(R.drawable.ic_transport)
                }
                "Food" -> {
                    categoryImage.setImageResource(R.drawable.ic_food)
                }
                "Utilities" -> {
                    categoryImage.setImageResource(R.drawable.ic_utilities)
                }
                "Insurance" -> {
                    categoryImage.setImageResource(R.drawable.ic_insurance)
                }
                "Healthcare" -> {
                    categoryImage.setImageResource(R.drawable.ic_medical)
                }
                "Saving & Debts" -> {
                    categoryImage.setImageResource(R.drawable.ic_savings)
                }
                "Personal Spending" -> {
                    categoryImage.setImageResource(R.drawable.ic_personal_spending)
                }
                "Entertainment" -> {
                    categoryImage.setImageResource(R.drawable.ic_entertainment)
                }
                "Miscellaneous" -> {
                    categoryImage.setImageResource(R.drawable.ic_others)
                }
                else -> {
                    categoryImage.setImageResource(R.drawable.ic_others)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return oldExpenseList.size
    }

    fun setData(newExpenseList: List<Expense>) {
        val diffUtil = ExpensesDiffUtil(oldExpenseList, newExpenseList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldExpenseList = newExpenseList
        diffResults.dispatchUpdatesTo(this)
    }

}