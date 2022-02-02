package com.developer.finance.presentation.expensesFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.finance.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    init {
        getExpenses("overall")
    }

    private val _state = MutableStateFlow<ExpensesFragmentEvent>(ExpensesFragmentEvent.Empty)
    val state: StateFlow<ExpensesFragmentEvent> = _state

    private val _transactionFilter = MutableStateFlow<String>("")
    val transactionFilter: StateFlow<String> = _transactionFilter

    fun getExpenses(transactionType: String) {
        expenseRepository.getAllExpensesFlow(transactionType).onEach { result ->
            if (result.isNullOrEmpty()) {
                _state.value = ExpensesFragmentEvent.Empty
            } else {
                Log.d("SUCCESS", result.toString())
                _state.value = ExpensesFragmentEvent.Success(result)
            }
        }.launchIn(viewModelScope)
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
