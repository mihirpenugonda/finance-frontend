package com.developer.finance.di

import android.content.Context
import androidx.room.Room
import com.developer.finance.data.local.TransactionDatabase
import com.developer.finance.data.repository.TransactionRepositoryImpl
import com.developer.finance.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providesExpenseRepository(
        db: TransactionDatabase
    ): TransactionRepository {
        return TransactionRepositoryImpl(db.expenseDao)
    }

    @Singleton
    @Provides
    fun provideExpensesDatabase(@ApplicationContext context: Context): TransactionDatabase {
        return Room.databaseBuilder(
            context,
            TransactionDatabase::class.java,
            "transaction_management_d7"
        ).build()
    }
}
