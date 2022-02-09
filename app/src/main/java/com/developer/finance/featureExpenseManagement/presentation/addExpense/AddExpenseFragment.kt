package com.developer.finance.featureExpenseManagement.presentation.addExpense

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.developer.finance.R
import com.developer.finance.common.addTransaction.AddTransactionViewModel
import com.developer.finance.common.base.BaseFragment
import com.developer.finance.common.components.DatePickerText
import com.developer.finance.databinding.FragmentAddExpenseTabBinding
import com.developer.finance.featureExpenseManagement.Constants
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class AddExpenseFragment : BaseFragment<FragmentAddExpenseTabBinding>() {
    val viewModel: AddTransactionViewModel by activityViewModels<AddTransactionViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddExpenseTabBinding =
        FragmentAddExpenseTabBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        setupForm()
    }

    private fun initViews() {
        binding.addExpenseButton.setOnClickListener {
            val expense = handleSubmit()

            if (expense != null) {
                lifecycleScope.launch {
                    viewModel.addExpense(expense)
                }
                activity?.finish()
            } else {
                Log.d("ERROR", "ERROR")
            }
        }
    }

    private fun setupForm() = with(context) {
        DatePickerText(requireContext(), binding.datePicker)

        val categoryAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            Constants.transactionCategory.filter { it != "all" }
        )
        binding.categoryPicker.setAdapter(categoryAdapter)

        val typeAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            Constants.transactionTypes.filter { it != "overall" }
        )
        binding.typePicker.setAdapter(typeAdapter)

        val frequencyAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            Constants.transactionFrequency
        )
        binding.frequencyPicker.setAdapter(frequencyAdapter)

        val listOfElements = listOf(
            binding.nameExpense,
            binding.descriptionExpense,
            binding.amountExpense,
            binding.datePicker,
            binding.categoryPicker,
            binding.typePicker
        )

        listOfElements.forEach {
            it.doOnTextChanged { _, _, _, _ ->
                val layout = it.parent.parent as TextInputLayout
                layout.isErrorEnabled = false
            }
        }
    }

    private fun handleSubmit(): Transaction? {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val date: Long = if (binding.datePicker.text.toString() != "") {
            val dateObject: Long = formatter.parse(binding.datePicker.text.toString())!!.time
            dateObject
        } else {
            0
        }

        val newExpense = Transaction(
            binding.nameExpense.text.toString(),
            binding.descriptionExpense.text.toString(),
            binding.amountExpense.text.toString(),
            date,
            binding.categoryPicker.text.toString(),
            binding.typePicker.text.toString(),
            binding.frequencyPicker.text.toString()
        )

        var isValidated = true

        if (newExpense.title.isEmpty()) {
            binding.nameExpenseParent.error = "name cant be empty"
            isValidated = false
        }
        if (newExpense.description.isEmpty()) {
            binding.descriptionExpenseParent.error = "description cant be empty"
            isValidated = false
        }
        if (newExpense.amount.isEmpty()) {
            binding.amountExpenseParent.error = "amount cant be empty"
            isValidated = false
        }
        try {
            Integer.parseInt(binding.amountExpense.text.toString())
        } catch (e: NumberFormatException) {
            binding.amountExpenseParent.error = "enter valid number"
            isValidated = false
        }
        if (newExpense.date.toString() == "0") {
            binding.datePickerParent.error = "date cant be empty"
            isValidated = false
        }
        if (newExpense.category.isEmpty()) {
            binding.categoryPickerParent.error = "category cant be empty"
            isValidated = false
        }
        if (newExpense.type.isEmpty()) {
            binding.typePickerParent.error = "type cant be empty"
            isValidated = false
        }
        return if (isValidated) { newExpense } else { null }
    }

}

