package com.developer.finance.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.developer.finance.common.UIModeDataStore
import com.developer.finance.common.UIModeImpl
import com.developer.finance.data.local.ExpenseDao
import com.developer.finance.data.local.ExpenseDatabase
import com.developer.finance.data.repository.ExpenseRepositoryImpl
import com.developer.finance.domain.repository.ExpenseRepository
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
    fun providesUIDataStore(@ApplicationContext context: Context): UIModeImpl {
        return UIModeDataStore(context)
    }

    @Singleton
    @Provides
    fun providesExpenseRepository(
        db: ExpenseDatabase
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(db.expenseDao)
    }

    @Singleton
    @Provides
    fun provideExpensesDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return Room.databaseBuilder(context, ExpenseDatabase::class.java, "expense_management_db").build()
    }

}