package com.developer.finance.domain.repository

import com.developer.finance.data.local.entity.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun createExpense(expense: Expense)
    suspend fun deleteExpense(id: Int)
    fun getAllExpensesFlow(transactionType: String): Flow<List<Expense>>
    suspend fun getAllExpense(): List<Expense>
    suspend fun deleteAllExpenses()
}