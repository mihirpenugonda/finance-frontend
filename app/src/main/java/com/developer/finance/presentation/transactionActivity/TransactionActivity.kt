package com.developer.finance.presentation.transactionActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.finance.data.local.entity.Expense
import com.developer.finance.databinding.ActivityTransactionBinding
import com.developer.finance.presentation.transactionActivity.adapter.TransactionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionBinding
    private val adapter by lazy {
        TransactionAdapter()
    }
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel

        initRv()
        launchExpenseListener()
    }

    private fun initRv() {
        val layoutManager: LinearLayoutManager = LinearLayoutManager(applicationContext)
        binding.transactionsRecyclerView.adapter = adapter
        binding.transactionsRecyclerView.layoutManager = layoutManager
    }

    private fun launchExpenseListener() =
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { event ->
                when (event) {
                    is TransactionsFragmentEvent.Empty -> {
                    }
                    is TransactionsFragmentEvent.Success -> {
                        updateRv(event.transactions)
                    }
                    else -> {}
                }
            }
        }

    private fun updateRv(newExpenses: List<Expense>) {
        adapter.setData(newExpenses)
    }
}