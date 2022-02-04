package com.developer.finance.presentation.addTransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.finance.data.local.entity.Transaction
import com.developer.finance.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val expenseRepository: TransactionRepository
) : ViewModel() {

    private var getExpensesJob: Job? = null

    private var searchJob: Job? = null

    init {

    }

    suspend fun addExpense(expense: Transaction) {
        searchJob?.cancel()
        viewModelScope.launch {
            expenseRepository.createExpense(expense)
        }
    }
}