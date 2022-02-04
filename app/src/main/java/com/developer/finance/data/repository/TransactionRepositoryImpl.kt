package com.developer.finance.data.repository

import com.developer.finance.data.local.TransactionDao
import com.developer.finance.data.local.entity.Transaction
import com.developer.finance.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class TransactionRepositoryImpl(
    private val expenseDao: TransactionDao
) : TransactionRepository {
    override suspend fun createExpense(expense: Transaction) {
        expenseDao.insertExpense(expense)
    }

    override suspend fun deleteExpense(id: Int) {
        expenseDao.deleteExpense(id)
    }

    override fun getAllExpensesFlow(transactionType: String): Flow<List<Transaction>> {
        return if (transactionType == "overall") expenseDao.getAllExpenseFlow()
        else expenseDao.getAllSingleExpenseFlow(transactionType)
    }

    override suspend fun getQueryExpenses(search: String): List<Transaction> {
        return expenseDao.getQueryExpenses(search)
    }

    override suspend fun getAllExpense(): List<Transaction> {
        return expenseDao.getAllExpense()
    }

    override suspend fun deleteAllExpenses() {
        expenseDao.deleteAllExpenses()
    }

    override suspend fun getCategoryExpense(category: String): List<Transaction> {
        return expenseDao.getCategoryExpenses(category)
    }

}