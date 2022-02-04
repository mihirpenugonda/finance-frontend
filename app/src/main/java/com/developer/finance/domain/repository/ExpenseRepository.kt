package com.developer.finance.domain.repository

import com.developer.finance.data.local.entity.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun createExpense(expense: Expense)
    suspend fun deleteExpense(id: Int)
    suspend fun getAllExpense(): List<Expense>
    suspend fun deleteAllExpenses()
    suspend fun getCategoryExpense(categories: List<String>): List<Expense>

    fun getAllExpensesFlow(transactionType: String): Flow<List<Expense>>
    fun getQueryExpensesFlow(search: String): Flow<List<Expense>>
}