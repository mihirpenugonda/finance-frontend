package com.developer.finance.featureExpenseManagement.presentation.transactionDashboardFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.finance.featureExpenseManagement.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TransactionDashboardViewModel @Inject constructor(
    private val expenseRepository: TransactionRepository
) : ViewModel() {

    init {
        getExpenses("overall")
    }

    private val _state =
        MutableStateFlow<TransactionDashboardFragmentEvent>(TransactionDashboardFragmentEvent.Empty)
    val state: StateFlow<TransactionDashboardFragmentEvent> = _state

    private val _transactionFilter = MutableStateFlow<String>("")
    val transactionFilter: StateFlow<String> = _transactionFilter

    fun getExpenses(transactionType: String) {
        expenseRepository.getAllExpensesFlow(transactionType).onEach { result ->
            if (result.isNullOrEmpty()) {
                _state.value = TransactionDashboardFragmentEvent.Empty
            } else {
                _state.value = TransactionDashboardFragmentEvent.Success(result)
            }
        }.launchIn(viewModelScope)
    }

    suspend fun deleteAll() {
        expenseRepository.deleteAllExpenses()
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
