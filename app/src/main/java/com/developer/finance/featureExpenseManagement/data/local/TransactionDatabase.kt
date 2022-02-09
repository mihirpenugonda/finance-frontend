package com.developer.finance.featureExpenseManagement.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.developer.finance.featureExpenseManagement.data.local.entity.RecurringTransactions
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction

@Database(
    entities = [Transaction::class, RecurringTransactions::class],
    version = 4
)

abstract class TransactionDatabase : RoomDatabase() {
    abstract val transactionsDao: TransactionDao
    abstract val recurringTransactionsDao: RecurringTransactionsDao
}