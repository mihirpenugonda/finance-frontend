package com.developer.finance.featureExpenseManagement.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction

@Database(
    entities = [Transaction::class],
    version = 3
)

abstract class TransactionDatabase : RoomDatabase() {
    abstract val expenseDao: TransactionDao
}