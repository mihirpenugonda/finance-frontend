package com.developer.finance.featureExpenseManagement.presentation.allTransactionActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.finance.common.DateTimeConverter
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction
import com.developer.finance.featureExpenseManagement.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
        MutableStateFlow<AllTransactionsEvent>(AllTransactionsEvent.Empty)
    val state: StateFlow<AllTransactionsEvent> = _state

    var queryJob: Job? = null

    init {
        getExpensesVM()
    }

    private fun getExpensesVM() {
        queryJob?.cancel()
        queryJob = expenseRepository.getAllExpensesFlow("overall").onEach { result ->
            delay(500)
            if (result.isNullOrEmpty()) {
                _state.value = AllTransactionsEvent.Empty
            } else {
                _state.value = AllTransactionsEvent.Success(result)
            }
        }.launchIn(viewModelScope)
    }

    fun getExpenses(
        search: String,
        category: String,
        type: String,
        startDate: Long,
        endDate: Long
    ) {
        queryJob?.cancel()
        queryJob = viewModelScope.launch {
            delay(500)
//            Log.d("Expenses Filters", "$search $category $type")
            var result = expenseRepository.getQueryExpenses(search)

            // Category Filter
            if (category != "all") {
                result = result.filter { it.category == category }
            }

            // Type Filter
            if (type != "overall") {
                result = result.filter { it.type == type }
            }

            if (startDate != 0L && endDate != 0L) {
                result = result.filter { it.date in startDate..endDate }
            }

            // Send out Result Event
            if (result.isNullOrEmpty()) {
                _state.value = AllTransactionsEvent.Empty
            } else {
                _state.value = AllTransactionsEvent.Success(result)
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
    }
}