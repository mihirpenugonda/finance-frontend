package com.developer.finance.featureExpenseManagement.presentation.transactionActivity

import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction

sealed class TransactionEvent {
    object Loading : TransactionEvent()
    data class Success(var transaction: Transaction) : TransactionEvent()
    data class Error(var message: String) : TransactionEvent()
    object Empty : TransactionEvent()
}