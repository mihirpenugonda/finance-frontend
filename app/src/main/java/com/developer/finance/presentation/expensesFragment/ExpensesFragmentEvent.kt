package com.developer.finance.presentation.expensesFragment

import com.developer.finance.data.local.entity.Transaction

sealed class ExpensesFragmentEvent {
    object Loading : ExpensesFragmentEvent()
    data class Success(var expenses: List<Transaction>) : ExpensesFragmentEvent()
    data class Error(var message: String) : ExpensesFragmentEvent()
    object Empty : ExpensesFragmentEvent()
}