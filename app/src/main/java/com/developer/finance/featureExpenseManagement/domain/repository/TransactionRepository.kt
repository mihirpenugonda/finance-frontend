package com.developer.finance.featureExpenseManagement.domain.repository

import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun createExpense(expense: Transaction)
    suspend fun deleteExpense(id: Int)
    suspend fun getAllExpense(): List<Transaction>
    suspend fun deleteAllExpenses()
    suspend fun getCategoryExpense(category: String): List<Transaction>
    suspend fun getExpenseById(id: Int): Transaction?
    suspend fun testFunctionForService()

    fun getAllExpensesFlow(transactionType: String): Flow<List<Transaction>>
    suspend fun getQueryExpenses(search: String): List<Transaction>
}