package com.developer.finance.presentation.allTransactionActivity

import com.developer.finance.data.local.entity.Transaction

sealed class TransactionsFragmentEvent {
    object Loading : TransactionsFragmentEvent()
    data class Success(var transactions: List<Transaction>) : TransactionsFragmentEvent()
    data class Error(var message: String) : TransactionsFragmentEvent()
    object Empty : TransactionsFragmentEvent()
}