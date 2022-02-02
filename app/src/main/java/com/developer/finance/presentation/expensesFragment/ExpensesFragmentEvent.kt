package com.developer.finance.presentation.expensesFragment

import com.developer.finance.data.local.entity.Expense

sealed class ExpensesFragmentEvent {
    object Loading : ExpensesFragmentEvent()
    data class Success(var expenses: List<Expense>) : ExpensesFragmentEvent()
    data class Error(var message: String) : ExpensesFragmentEvent()
    object Empty : ExpensesFragmentEvent()
}