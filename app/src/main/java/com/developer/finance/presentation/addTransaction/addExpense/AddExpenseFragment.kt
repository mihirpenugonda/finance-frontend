package com.developer.finance.presentation.addTransaction.addExpense

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
import com.developer.finance.common.Constants
import com.developer.finance.common.base.BaseFragment
import com.developer.finance.data.local.entity.Expense
import com.developer.finance.databinding.FragmentAddExpenseTabBinding
import com.developer.finance.presentation.addTransaction.AddTransactionViewModel
import com.developer.finance.presentation.addTransaction.components.DatePickerText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class AddExpenseFragment : BaseFragment<FragmentAddExpenseTabBinding, AddTransactionViewModel>() {
    override val viewModel: AddTransactionViewModel by activityViewModels<AddTransactionViewModel>()

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
        binding.addButton.setOnClickListener {
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
            Constants.transactionCategory
        )
        binding.categoryPicker.setAdapter(categoryAdapter)

        val typeAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            Constants.transactionTypes
        )
        binding.typePicker.setAdapter(typeAdapter)

        binding.nameExpense.doOnTextChanged { _, _, _, _ ->
            binding.nameExpenseParent.isErrorEnabled = false
        }
        binding.descriptionExpense.doOnTextChanged { _, _, _, _ ->
            binding.descriptionExpenseParent.isErrorEnabled = false
        }
        binding.amountExpense.doOnTextChanged { _, _, _, _ ->
            binding.amountExpenseParent.isErrorEnabled = false
        }
        binding.datePicker.doOnTextChanged { _, _, _, _ ->
            binding.datePickerParent.isErrorEnabled = false
        }
        binding.categoryPicker.doOnTextChanged { _, _, _, _ ->
            binding.categoryPickerParent.isErrorEnabled = false
        }
        binding.typePicker.doOnTextChanged { _, _, _, _ ->
            binding.typePickerParent.isErrorEnabled = false
        }
    }

    private fun handleSubmit(): Expense? {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val date: String = if (binding.datePicker.text.toString() != "") {
            val dateObject: Date = formatter.parse(binding.datePicker.text.toString())!!
            SimpleDateFormat("dd/MM/yyyy", Locale.US).format(dateObject)
        } else {
            ""
        }

        val newExpense = Expense(
            binding.nameExpense.text.toString(),
            binding.descriptionExpense.text.toString(),
            binding.amountExpense.text.toString(),
            date,
            binding.categoryPicker.text.toString(),
            binding.typePicker.text.toString()
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
        if (newExpense.date.isEmpty()) {
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

        return if (isValidated) {
            newExpense
        } else {
            null
        }
    }

}

