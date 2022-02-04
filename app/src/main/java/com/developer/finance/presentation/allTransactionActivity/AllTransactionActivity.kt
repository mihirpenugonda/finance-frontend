package com.developer.finance.presentation.allTransactionActivity

import android.os.Bundle
import android.util.Log
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
import com.developer.finance.common.Constants
import com.developer.finance.data.local.entity.Transaction
import com.developer.finance.databinding.ActivityAllTransactionsBinding
import com.developer.finance.presentation.allTransactionActivity.adapter.AllTransactionAdapter
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

    private val viewModelAll: AllTransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setupSpinnerListeners()
        initRv()
        launchEventListener()
        launchSearchListener()

        Log.d("When Created", "$categoryFilter $typeFilter")
    }

    private fun initViews() {
        binding.transactionFilterViewButton.setOnClickListener {
            val transition: Transition = Fade()
            transition.duration = 600
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
                    Log.d("Category Spinner", "$categoryFilter $typeFilter")
                    viewModelAll.getExpenses(searchFilter, categoryFilter, typeFilter)
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
                    viewModelAll.getExpenses(searchFilter, categoryFilter, typeFilter)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun initRv() {
        val layoutManager: LinearLayoutManager = LinearLayoutManager(applicationContext)
        binding.transactionsRecyclerView.adapter = adapter
        binding.transactionsRecyclerView.layoutManager = layoutManager
    }

    private fun launchEventListener() =
        lifecycleScope.launchWhenStarted {
            viewModelAll.state.collect { event ->
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

    private fun launchSearchListener() {
        binding.searchInput.doOnTextChanged { text, _, _, _ ->
            searchFilter = text.toString()
            viewModelAll.getExpenses(searchFilter, categoryFilter, typeFilter)
        }
    }

    private fun updateRv(newExpenses: List<Transaction>) {
        adapter.setData(newExpenses)
    }
}