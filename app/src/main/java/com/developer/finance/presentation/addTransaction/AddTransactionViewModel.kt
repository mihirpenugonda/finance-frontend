package com.developer.finance.presentation.addTransaction

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.finance.data.local.entity.Expense
import com.developer.finance.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddTransactionState())
    val state: StateFlow<AddTransactionState> = _state

    private var getExpensesJob: Job? = null

    private var searchJob: Job? = null

    init {

    }

    suspend fun addExpense(expense: Expense) {
        searchJob?.cancel()
        viewModelScope.launch {
            expenseRepository.createExpense(expense)
        }
    }
}