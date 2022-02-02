package com.developer.finance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.developer.finance.data.local.entity.Expense

@Database(
    entities = [Expense::class],
    version = 2
)

abstract class ExpenseDatabase : RoomDatabase() {
    abstract val expenseDao: ExpenseDao
}