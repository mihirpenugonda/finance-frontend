package com.developer.finance.presentation.transactionActivity

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
class TransactionViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _state = MutableStateFlow<TransactionsFragmentEvent>(TransactionsFragmentEvent.Empty)
    val state: StateFlow<TransactionsFragmentEvent> = _state

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
}