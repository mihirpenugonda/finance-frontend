package com.developer.finance.featureExpenseManagement.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developer.finance.featureExpenseManagement.data.local.entity.RecurringTransactions

@Dao
interface RecurringTransactionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecurringTransaction(expense: RecurringTransactions)

    @Query("DELETE FROM recurringtransactions WHERE t_id IN (:id)")
    suspend fun deleteRecurringTransaction(id: kotlin.Int)

    @Query("SELECT * FROM recurringtransactions WHERE t_id IN (:id)")
    suspend fun getRecurringTransactionById(id: Long): RecurringTransactions

    @Query("SELECT * FROM recurringtransactions")
    suspend fun getRecurringTransactions(): List<RecurringTransactions>
}