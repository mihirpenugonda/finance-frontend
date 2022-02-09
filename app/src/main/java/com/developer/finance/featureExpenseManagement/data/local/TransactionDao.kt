package com.developer.finance.featureExpenseManagement.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Transaction): Long

    @Query("DELETE FROM `transaction` WHERE t_id IN (:id)")
    suspend fun deleteExpense(id: Int)

    @Query("SELECT * FROM `transaction` WHERE  t_id IN (:id)")
    suspend fun getExpenseById(id: Int): Transaction?

    @Query("SELECT * FROM `transaction` WHERE title LIKE '%'||:search||'%' OR description LIKE '%'||:search||'%' ORDER BY date DESC")
    suspend fun getQueryExpenses(search: String): List<Transaction>

    @Query("SELECT * FROM `transaction` WHERE category in (:category)")
    suspend fun getCategoryExpenses(category: String): List<Transaction>

    @Query("SELECT * FROM `transaction`")
    suspend fun getAllExpense(): List<Transaction>

    @Query("Delete from `transaction`")
    suspend fun deleteAllExpenses()

    @Query("SELECT * FROM `transaction` WHERE type in (:type) ORDER BY date DESC")
    fun getAllSingleExpenseFlow(type: String): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    fun getAllExpenseFlow(): Flow<List<Transaction>>
}