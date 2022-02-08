package com.developer.finance.featureExpenseManagement.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction
import com.developer.finance.featureExpenseManagement.domain.repository.TransactionRepository
import com.developer.finance.featureExpenseManagement.presentation.allTransactionActivity.AllTransactionsEvent
import com.developer.finance.featureExpenseManagement.presentation.transactionActivity.TransactionEvent
import com.developer.finance.featureExpenseManagement.presentation.transactionDashboardFragment.TransactionDashboardFragmentEvent
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
class ExpenseViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _dashboardState =
        MutableStateFlow<TransactionDashboardFragmentEvent>(TransactionDashboardFragmentEvent.Empty)
    val dashboardState: StateFlow<TransactionDashboardFragmentEvent> = _dashboardState

    private val _dashboardFilter = MutableStateFlow("")
    val dashboardFilter: StateFlow<String> = _dashboardFilter

    private val _transactionState = MutableStateFlow<TransactionEvent>(TransactionEvent.Empty)
    val transactionState: StateFlow<TransactionEvent> = _transactionState

    private val _allTransactionsState =
        MutableStateFlow<AllTransactionsEvent>(AllTransactionsEvent.Empty)
    val allTransactionsState: StateFlow<AllTransactionsEvent> = _allTransactionsState

    private val _transactionStateId = MutableStateFlow(0)
    val transactionStateId: StateFlow<Int> = _transactionStateId

    var dashboardJob: Job? = null
    var transactionJob: Job? = null
    var allTransactionJob: Job? = null

    // Transaction Dashboard Methods
    fun getDashboardExpenses(transactionType: String) {
        dashboardJob = transactionRepository.getAllExpensesFlow(transactionType).onEach { result ->
            Log.d("Dashboard Expenses", result.toString())
            if (result.isNullOrEmpty()) {
                _dashboardState.value = TransactionDashboardFragmentEvent.Empty
            } else {
                _dashboardState.value = TransactionDashboardFragmentEvent.Success(result)
            }
        }.launchIn(viewModelScope)
    }

    fun setFilter(filter: String) {
        _dashboardFilter.value = filter
    }

    // All Transactions Methods
    fun getAllTransactions() {
        allTransactionJob?.cancel()
        allTransactionJob =
            transactionRepository.getAllExpensesFlow("overall").onEach { result ->
                if (result.isNullOrEmpty()) {
                    _allTransactionsState.value = AllTransactionsEvent.Empty
                } else {
                    _allTransactionsState.value = AllTransactionsEvent.Success(result)
                }
            }.launchIn(viewModelScope)
    }

    fun getFilterTransactions(
        search: String,
        category: String,
        type: String,
        startDate: Long,
        endDate: Long
    ) {
        allTransactionJob?.cancel()
        allTransactionJob = viewModelScope.launch {
            delay(500)
//            Log.d("Expenses Filters", "$search $category $type")
            var result = transactionRepository.getQueryExpenses(search)

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
                _allTransactionsState.value = AllTransactionsEvent.Empty
            } else {
                _allTransactionsState.value = AllTransactionsEvent.Success(result)
            }
        }
    }

    // Transaction Methods
    private fun getExpenseById(id: Int) {
        viewModelScope.launch {
            val result = transactionRepository.getExpenseById(id)
            if (result == null) {
                _transactionState.value = TransactionEvent.Empty
            } else {
                _transactionState.value = TransactionEvent.Success(result)
            }
        }
    }

    fun updateExpense(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.createExpense(transaction)
        }
    }

    fun setTransactionId(id: Int) {
        _transactionStateId.value = id
        getExpenseById(id)
    }

    fun deleteExpense(id: Int) {
        viewModelScope.launch {
            transactionRepository.deleteExpense(id)
        }
    }
}