package com.developer.finance.featureExpenseManagement.presentation.transactionActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.developer.finance.R
import com.developer.finance.common.DateTimeConverter
import com.developer.finance.common.components.DatePickerText
import com.developer.finance.databinding.ActivityTransactionBinding
import com.developer.finance.featureExpenseManagement.Constants
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction
import com.developer.finance.featureExpenseManagement.presentation.ExpenseViewModel
import com.developer.finance.featureExpenseManagement.presentation.allTransactionActivity.AllTransactionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionBinding
    private var transactionId = 0

    private val transactionViewModel: ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        launchEventListener()
    }

    private fun initViews() {
        transactionId = intent.getIntExtra("transaction_id", 0)
        transactionViewModel.setTransactionId(transactionId)

        Log.d("Categories", Constants.transactionCategory.toString())

        val categoryAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.dropdown_item,
            Constants.transactionCategory.filter { it != "all" }
        )

        val typeAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.dropdown_item,
            Constants.transactionTypes.filter { it != "overall" }
        )

        val frequencyAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.dropdown_item,
            Constants.transactionFrequency
        )

        DatePickerText(this, binding.transactionDatePicker)

        binding.transactionCategory.setAdapter(categoryAdapter)
        binding.transactionType.setAdapter(typeAdapter)
        binding.transactionFrequency.setAdapter(frequencyAdapter)

        binding.transactionUpdateExpenseButton.setOnClickListener {
            transactionViewModel.updateExpense(
                Transaction(
                    binding.transactionName.text.toString(),
                    binding.transactionDescription.text.toString(),
                    binding.transactionAmount.text.toString(),
                    DateTimeConverter().formatToMillis(binding.transactionDatePicker.text.toString()),
                    binding.transactionCategory.text.toString(),
                    binding.transactionType.text.toString(),
                    binding.transactionFrequency.text.toString(),
                    System.currentTimeMillis(),
                    transactionId
                )
            )
            finish()
        }

        binding.transactionDeleteExpenseButton.setOnClickListener {
            transactionViewModel.deleteExpense(transactionId)
            val intent = Intent(this, AllTransactionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun launchEventListener() {
        lifecycleScope.launchWhenStarted {
            transactionViewModel.transactionState.collect { event ->
                when (event) {
                    is TransactionEvent.Empty -> Log.d(
                        "TransactionEvent:",
                        "transaction does not exist"
                    )
                    is TransactionEvent.Success -> {
                        binding.transactionName.setText(event.transaction.title)
                        binding.transactionDescription.setText(event.transaction.description)
                        binding.transactionAmount.setText(event.transaction.amount)
                        binding.transactionCategory.setText(event.transaction.category)
                        binding.transactionDatePicker.setText(DateTimeConverter().format(event.transaction.date))
                        binding.transactionType.setText(event.transaction.type)
                        binding.transactionFrequency.setText(event.transaction.frequency)
                    }
                    else -> {}
                }
            }
        }
    }
}