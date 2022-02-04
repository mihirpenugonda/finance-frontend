package com.developer.finance.presentation.transactionActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.finance.data.local.entity.Expense
import com.developer.finance.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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

    init {
        getExpenses("overall")
    }

    private fun getExpenses(transactionType: String) {
        expenseRepository.getAllExpensesFlow(transactionType).onEach { result ->
            if (result.isNullOrEmpty()) {
                _state.value = TransactionsFragmentEvent.Empty
            } else {
                _state.value = TransactionsFragmentEvent.Success(result)
            }
        }.launchIn(viewModelScope)
    }

    fun getExpenses(search: String,category: String, type: String) {
        viewModelScope.launch {
            Log.d("Expenses Filters", "$search $category $type")
            var result = expenseRepository.getQueryExpenses(search)
            Log.d("Result", result.toString())

            if(category != "all") {
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
}