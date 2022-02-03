package com.developer.finance.presentation.transactionActivity

import com.developer.finance.data.local.entity.Expense

sealed class TransactionsFragmentEvent {
    object Loading : TransactionsFragmentEvent()
    data class Success(var transactions: List<Expense>) : TransactionsFragmentEvent()
    data class Error(var message: String) : TransactionsFragmentEvent()
    object Empty : TransactionsFragmentEvent()
}