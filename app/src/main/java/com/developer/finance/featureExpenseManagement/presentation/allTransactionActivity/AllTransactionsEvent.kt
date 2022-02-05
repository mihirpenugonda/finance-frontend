package com.developer.finance.featureExpenseManagement.presentation.allTransactionActivity

import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction

sealed class AllTransactionsEvent {
    object Loading : AllTransactionsEvent()
    data class Success(var transactions: List<Transaction>) : AllTransactionsEvent()
    data class Error(var message: String) : AllTransactionsEvent()
    object Empty : AllTransactionsEvent()
}