package com.developer.finance.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developer.finance.data.local.entity.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: Expense)

    @Query("DELETE FROM expense WHERE e_id IN (:id)")
    suspend fun deleteExpense(id: Int)

    @Query("SELECT * FROM expense ORDER BY created_at DESC")
    fun getAllExpenseFlow(): Flow<List<Expense>>

    @Query("SELECT * FROM expense WHERE title LIKE '%'||:search||'%' OR description LIKE '%'||:search||'%'")
    suspend fun getQueryExpenses(search: String): List<Expense>

    @Query("SELECT * FROM expense WHERE category in (:category)")
    suspend fun getCategoryExpenses(category: String): List<Expense>

    @Query("SELECT * FROM expense")
    suspend fun getAllExpense(): List<Expense>

    @Query("Delete from expense")
    suspend fun deleteAllExpenses()

    @Query("SELECT * FROM expense WHERE type in (:type) ORDER BY created_at DESC")
    fun getAllSingleExpenseFlow(type: String): Flow<List<Expense>>
}