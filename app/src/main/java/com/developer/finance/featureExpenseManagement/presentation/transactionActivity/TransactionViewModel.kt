package com.developer.finance.featureExpenseManagement.presentation.transactionActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction
import com.developer.finance.featureExpenseManagement.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow<TransactionEvent>(TransactionEvent.Empty)
    val state: StateFlow<TransactionEvent> = _state

    private val _transactionId = MutableStateFlow<Int>(0)
    val transactionId: StateFlow<Int> = _transactionId

    private fun getExpenseById(id: Int) {
        viewModelScope.launch {
            val result = transactionRepository.getExpenseById(id)
            if (result == null) {
                _state.value = TransactionEvent.Empty
            } else {
                _state.value = TransactionEvent.Success(result)
            }
        }
    }

    fun updateExpense(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.createExpense(transaction)
        }
    }

    fun setTransactionId(id: Int) {
        _transactionId.value = id
        getExpenseById(id)
    }
}