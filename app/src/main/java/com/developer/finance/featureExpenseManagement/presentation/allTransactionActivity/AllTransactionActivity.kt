package com.developer.finance.featureExpenseManagement.presentation.allTransactionActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.developer.finance.R
import com.developer.finance.common.DateTimeConverter
import com.developer.finance.common.components.DatePickerText
import com.developer.finance.databinding.ActivityAllTransactionsBinding
import com.developer.finance.featureExpenseManagement.Constants
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction
import com.developer.finance.featureExpenseManagement.presentation.ExpenseViewModel
import com.developer.finance.featureExpenseManagement.presentation.allTransactionActivity.adapter.AllTransactionAdapter
import com.developer.finance.featureExpenseManagement.presentation.transactionActivity.TransactionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class AllTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllTransactionsBinding

    private val adapter by lazy {
        AllTransactionAdapter()
    }

    private var filterViewVisible = false
    private var categoryFilter = "all"
    private var typeFilter = "overall"
    private var searchFilter = ""
    private var startDateFilter: Long = 0L
    private var endDateFilter: Long = 0L

    private val allTransactionViewModel: ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setupSpinnerListeners()
        launchEventListener()
        launchSearchListener()
        launchDateListeners()
        initRv()
    }

    private fun initViews() {
        binding.transactionFilterViewButton.setOnClickListener {
            val transition: Transition = Fade()
            transition.duration = 50
            transition.addTarget(binding.transactionFilterView)

            TransitionManager.beginDelayedTransition(
                binding.transactionFilterView,
                transition
            )

            if (filterViewVisible) {
                binding.transactionFilterView.visibility = View.VISIBLE
                filterViewVisible = !filterViewVisible
            } else {
                binding.transactionFilterView.visibility = View.GONE
                filterViewVisible = !filterViewVisible
            }
        }

        DatePickerText(this, binding.startDatePicker)
        DatePickerText(this, binding.endDatePicker)
    }

    private fun setupSpinnerListeners() {
        val categoryAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.transactionCategorySpinner.adapter = categoryAdapter
        binding.transactionCategorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    categoryFilter = Constants.transactionCategory[position]
//                    Log.d("Category Spinner", "$categoryFilter $typeFilter")
                    allTransactionViewModel.getFilterTransactions(
                        searchFilter,
                        categoryFilter,
                        typeFilter,
                        startDateFilter,
                        endDateFilter
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

        val typeAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.types,
            android.R.layout.simple_spinner_item
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.transactionTypeSpinner.adapter = typeAdapter
        binding.transactionTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    typeFilter = Constants.transactionTypes[position]
                    allTransactionViewModel.getFilterTransactions(
                        searchFilter,
                        categoryFilter,
                        typeFilter,
                        startDateFilter,
                        endDateFilter
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun initRv() {
        val layoutManager: LinearLayoutManager = LinearLayoutManager(applicationContext)
        binding.transactionsRecyclerView.adapter = adapter
        binding.transactionsRecyclerView.layoutManager = layoutManager

        adapter.setOnItemClickListener { transaction ->
            val intent = Intent(this, TransactionActivity::class.java)
            intent.putExtra("transaction_id", transaction.id)
            startActivity(intent)
            finish()
        }
    }

    private fun launchEventListener() {
        lifecycleScope.launchWhenStarted {
            allTransactionViewModel.allTransactionsState.collect { event ->
                when (event) {
                    is AllTransactionsEvent.Empty -> {
                    }
                    is AllTransactionsEvent.Success -> {
                        updateRv(event.transactions)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun launchSearchListener() {
        binding.searchInput.doOnTextChanged { text, _, _, _ ->
            searchFilter = text.toString()
            allTransactionViewModel.getFilterTransactions(
                searchFilter,
                categoryFilter,
                typeFilter,
                startDateFilter,
                endDateFilter
            )
        }
    }

    // TODO: Finish Date Listeners
    private fun launchDateListeners() {

        binding.startDatePicker.doOnTextChanged { text, _, _, _ ->
//            Log.d("startDateFilter", "$startDateFilter $endDateFilter")
            startDateFilter = DateTimeConverter().formatToMillis(text.toString())
            if (endDateFilter != 0L) {
                allTransactionViewModel.getFilterTransactions(
                    searchFilter,
                    categoryFilter,
                    typeFilter,
                    startDateFilter,
                    endDateFilter
                )

            }
        }

        binding.endDatePicker.doOnTextChanged { text, _, _, _ ->
//            Log.d("endDateFilter", "$startDateFilter $endDateFilter")
            endDateFilter =
                DateTimeConverter().formatToMillis(text.toString()).plus(1000 * 60 * 60 * 24) - 1
            if (startDateFilter != 0L) {
                allTransactionViewModel.getFilterTransactions(
                    searchFilter,
                    categoryFilter,
                    typeFilter,
                    startDateFilter,
                    endDateFilter
                )
            }
        }
    }

    private fun updateRv(newExpenses: List<Transaction>) {
        adapter.expenseList = newExpenses
    }

    override fun onResume() {
        super.onResume()
        allTransactionViewModel.getAllTransactions()
    }
}