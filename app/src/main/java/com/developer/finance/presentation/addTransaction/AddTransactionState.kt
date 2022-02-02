package com.developer.finance.presentation.addTransaction

import com.developer.finance.data.local.entity.Expense

data class AddTransactionState (
    var expenses: List<Expense> = emptyList()
)