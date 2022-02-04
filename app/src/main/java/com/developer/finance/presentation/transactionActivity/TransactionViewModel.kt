package com.developer.finance.presentation.transactionActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.finance.domain.repository.ExpenseRepository
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
class TransactionViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow<TransactionsFragmentEvent>(TransactionsFragmentEvent.Empty)
    val state: StateFlow<TransactionsFragmentEvent> = _state

    var queryJob: Job? = null

    private val _transactionFilter = MutableStateFlow<String>("")
    val transactionFilter: StateFlow<String> = _transactionFilter

    init {
        getExpenses("overall")
    }

    fun getExpenses(transactionType: String) {
        expenseRepository.getAllExpensesFlow(transactionType).onEach { result ->
            if (result.isNullOrEmpty()) {
                _state.value = TransactionsFragmentEvent.Empty
            } else {
                _state.value = TransactionsFragmentEvent.Success(result)
            }
        }.launchIn(viewModelScope)
    }

    fun getQueryExpenses(search: String) {
        queryJob?.cancel()
        queryJob = expenseRepository.getQueryExpensesFlow(search).onEach { result ->
            delay(200)
            if (result.isNullOrEmpty()) {
                _state.value = TransactionsFragmentEvent.Empty
            } else {
                _state.value = TransactionsFragmentEvent.Success(result)
            }
        }.launchIn(viewModelScope)
    }

    fun getCategoryExpenses(categories: List<String>) {
        viewModelScope.launch {
            val result =
                expenseRepository.getCategoryExpense(categories)
            if (result.isNullOrEmpty()) {
                _state.value = TransactionsFragmentEvent.Empty
            } else {
                _state.value = TransactionsFragmentEvent.Success(result)
            }
        }
    }

    fun setOverall() {
        _transactionFilter.value = "overall"
    }

    fun setIncome() {
        _transactionFilter.value = "income"
    }

    fun setExpense() {
        _transactionFilter.value = "expense"
    }
}