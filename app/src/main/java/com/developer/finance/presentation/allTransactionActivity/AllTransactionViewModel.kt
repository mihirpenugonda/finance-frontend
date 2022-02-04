package com.developer.finance.presentation.allTransactionActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.finance.common.DateTimeConverter
import com.developer.finance.data.local.entity.Transaction
import com.developer.finance.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllTransactionViewModel @Inject constructor(
    private val expenseRepository: TransactionRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow<TransactionsFragmentEvent>(TransactionsFragmentEvent.Empty)
    val state: StateFlow<TransactionsFragmentEvent> = _state

    var queryJob: Job? = null

    init {
        getExpensesVM()
    }

    private fun getExpensesVM() {
        expenseRepository.getAllExpensesFlow("overall").onEach { result ->
            if (result.isNullOrEmpty()) {
                _state.value = TransactionsFragmentEvent.Empty
            } else {
                _state.value = TransactionsFragmentEvent.Success(result)
            }
        }.launchIn(viewModelScope)
    }

    fun getExpenses(search: String, category: String, type: String) {
        viewModelScope.launch {
            Log.d("Expenses Filters", "$search $category $type")
            var result = expenseRepository.getQueryExpenses(search)
            if (category != "all") {
                result = result.filter { it.category == category }
            }
            if (type != "overall") {
                result = result.filter { it.type == type }
            }
            if (result.isNullOrEmpty()) {
                _state.value = TransactionsFragmentEvent.Empty
            } else {
                _state.value = TransactionsFragmentEvent.Success(result)
            }
        }
    }

    fun splitData(transactions: List<Transaction>) {

        var prevDate = ""
        val output = mutableListOf<Any>()

        transactions.forEach {
            if (prevDate == "") {
                prevDate = DateTimeConverter().format(it.date)
                output.add(prevDate)
                output.add(it)
            } else {
                if (prevDate == DateTimeConverter().format(it.date)) {
                    output.add(it)
                } else {
                    prevDate = DateTimeConverter().format(it.date)
                    output.add(prevDate)
                    output.add(it)
                }
            }
        }

        var stringloc = 0
        var totalValue = 0
        output.forEachIndexed { i, item ->
            if (i == 0 && item is String) {
            } else {
                if (item is String) {
                    output[stringloc] = totalValue
                    totalValue = 0
                    stringloc = i
                } else {
                    if (item is Transaction) {
                        totalValue += Integer.parseInt(item.amount)
                    }
                }
            }
        }

        Log.d("Output", output.toString())
    }
}