package com.developer.finance.data.repository

import com.developer.finance.data.local.ExpenseDao
import com.developer.finance.data.local.entity.Expense
import com.developer.finance.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {
    override suspend fun createExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    override suspend fun deleteExpense(id: Int) {
        expenseDao.deleteExpense(id)
    }

    override fun getAllExpensesFlow(transactionType: String): Flow<List<Expense>> {
        return if(transactionType == "overall") expenseDao.getAllExpenseFlow()
        else expenseDao.getAllSingleExpenseFlow(transactionType)
    }

    override fun getQueryExpensesFlow(search: String): Flow<List<Expense>> {
        return expenseDao.getQueryExpensesFlow(search)
    }

    override suspend fun getAllExpense(): List<Expense> {
        return expenseDao.getAllExpense()
    }

    override suspend fun deleteAllExpenses() {
        expenseDao.deleteAllExpenses()
    }

    override suspend fun getCategoryExpense(categories: List<String>): List<Expense> {
        return expenseDao.getCategoryExpenses(categories)
    }

}