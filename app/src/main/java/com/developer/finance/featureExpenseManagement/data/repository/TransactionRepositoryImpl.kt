package com.developer.finance.featureExpenseManagement.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.developer.finance.featureExpenseManagement.Constants
import com.developer.finance.featureExpenseManagement.data.local.RecurringTransactionsDao
import com.developer.finance.featureExpenseManagement.data.local.TransactionDao
import com.developer.finance.featureExpenseManagement.data.local.entity.RecurringTransactions
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction
import com.developer.finance.featureExpenseManagement.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneOffset

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val recurringTransactionsDao: RecurringTransactionsDao
) : TransactionRepository {
    override suspend fun createExpense(expense: Transaction) {
        val t_id = transactionDao.insertExpense(expense)
        if (expense.frequency != "none") {
            recurringTransactionsDao.insertRecurringTransaction(
                RecurringTransactions(
                    expense.frequency,
                    System.currentTimeMillis() + Constants.transactionFrequencyToMillis[expense.frequency]!!,
                    t_id
                )
            )
        }
    }

    override suspend fun deleteExpense(id: Int) {
        transactionDao.deleteExpense(id)
        recurringTransactionsDao.deleteRecurringTransaction(id)
    }

    override fun getAllExpensesFlow(transactionType: String): Flow<List<Transaction>> {
        return if (transactionType == "overall") transactionDao.getAllExpenseFlow()
        else transactionDao.getAllSingleExpenseFlow(transactionType)
    }

    override suspend fun getQueryExpenses(search: String): List<Transaction> {
        return transactionDao.getQueryExpenses(search)
    }

    override suspend fun getAllExpense(): List<Transaction> {
        return transactionDao.getAllExpense()
    }

    override suspend fun deleteAllExpenses() {
        transactionDao.deleteAllExpenses()
    }

    override suspend fun getCategoryExpense(category: String): List<Transaction> {
        return transactionDao.getCategoryExpenses(category)
    }

    override suspend fun getExpenseById(id: Int): Transaction? {
        return transactionDao.getExpenseById(id.toLong())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun testFunctionForService() {
        val allRecurrenceTransactions = recurringTransactionsDao.getRecurringTransactions()

        allRecurrenceTransactions.forEach { recurrence ->
            val transaction = transactionDao.getExpenseById(recurrence.id)

            transaction!!.id = 0
            transaction.date =
                LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            transaction.created_at = System.currentTimeMillis()

            transactionDao.insertExpense(transaction)
            recurrence.nextRun =
                transaction.date + Constants.transactionFrequencyToMillis[transaction.frequency]!!

            recurringTransactionsDao.insertRecurringTransaction(recurrence)
        }
    }

}