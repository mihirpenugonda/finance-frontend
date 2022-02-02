package com.developer.finance.presentation.settingsFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.finance.common.UIModeImpl
import com.developer.finance.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val uiModeDataStore: UIModeImpl,
    private val expensesRepository: ExpenseRepository
) : ViewModel() {

    val getUIMode = uiModeDataStore.isDarkMode

    fun setUIMode(isNightMode: Boolean) {
        viewModelScope.launch(IO) {
            uiModeDataStore.saveToDataStore(isNightMode)
        }
    }

    fun isDarkModeSelected(): Boolean {
        var mode = false

        viewModelScope.launch(IO) {
            mode = uiModeDataStore.isDarkMode.last()
        }

        return mode
    }

    fun deleteAllExpenses() {
        viewModelScope.launch {
            expensesRepository.deleteAllExpenses()

        }
    }

}