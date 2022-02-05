package com.developer.finance.featureExpenseManagement.presentation.transactionActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.developer.finance.common.DateTimeConverter
import com.developer.finance.databinding.ActivityTransactionBinding
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionBinding
    private var transactionId = 0

    private val transactionViewModel: TransactionViewModel by viewModels()

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

        binding.transactionUpdateExpenseButton.setOnClickListener {
            transactionViewModel.updateExpense(
                Transaction(
                    binding.transactionName.text.toString(),
                    binding.transactionDescription.text.toString(),
                    binding.transactionAmount.text.toString(),
                    DateTimeConverter().formatToMillis(binding.transactionDatePicker.text.toString()),
                    binding.transactionCategory.text.toString(),
                    binding.transactionType.text.toString(),
                    System.currentTimeMillis(),
                    transactionId
                )
            )
            finish()
        }
    }

    private fun launchEventListener() {
        lifecycleScope.launchWhenStarted {
            transactionViewModel.state.collect { event ->
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
                    }
                    else -> {}
                }
            }
        }
    }
}