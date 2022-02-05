package com.developer.finance.featureExpenseManagement.presentation.transactionDashboardFragment

import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction

sealed class TransactionDashboardFragmentEvent {
    object Loading : TransactionDashboardFragmentEvent()
    data class Success(var expenses: List<Transaction>) : TransactionDashboardFragmentEvent()
    data class Error(var message: String) : TransactionDashboardFragmentEvent()
    object Empty : TransactionDashboardFragmentEvent()
}