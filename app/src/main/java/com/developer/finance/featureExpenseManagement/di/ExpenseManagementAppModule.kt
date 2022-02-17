package com.developer.finance.featureExpenseManagement.di

import android.content.Context
import androidx.room.Room
import com.developer.finance.featureExpenseManagement.data.local.TransactionDatabase
import com.developer.finance.featureExpenseManagement.data.repository.TransactionRepositoryImpl
import com.developer.finance.featureExpenseManagement.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ExpenseManagementAppModule {

    @Singleton
    @Provides
    fun providesExpenseRepository(
        db: TransactionDatabase
    ): TransactionRepository {
        return TransactionRepositoryImpl(db.transactionsDao, db.recurringTransactionsDao)
    }

    @Singleton
    @Provides
    fun provideExpensesDatabase(@ApplicationContext context: Context): TransactionDatabase {
        return Room.databaseBuilder(
            context,
            TransactionDatabase::class.java,
            "transaction_management12"
        ).build()
    }
}
