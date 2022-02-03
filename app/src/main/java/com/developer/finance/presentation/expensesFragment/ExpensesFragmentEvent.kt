package com.developer.finance.presentation.expensesFragment

import com.developer.finance.data.local.entity.Expense
import com.developer.finance.presentation.transactionActivity.TransactionsFragmentEvent

sealed class ExpensesFragmentEvent {
    object Loading : ExpensesFragmentEvent()
    data class Success(var expenses: List<Expense>) : ExpensesFragmentEvent()
    data class Error(var message: String) : ExpensesFragmentEvent()
    object Empty : ExpensesFragmentEvent()
}