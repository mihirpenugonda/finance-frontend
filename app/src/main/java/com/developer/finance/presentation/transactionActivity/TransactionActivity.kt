package com.developer.finance.presentation.transactionActivity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.finance.R
import com.developer.finance.common.Constants
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
    private var filterViewVisible = false
    private val categoriesFilter = mutableListOf<String>()

    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initRv()
        launchExpenseListener()
        launchSearchListener()
        launchFilterListener()
    }

    private fun initViews() {
        binding.transactionFilterViewButton.setOnClickListener {
            if (filterViewVisible) {
                binding.transactionFilterView.visibility = View.VISIBLE
                filterViewVisible = !filterViewVisible
            } else {
                binding.transactionFilterView.visibility = View.GONE
                filterViewVisible = !filterViewVisible
            }
        }

        val categoryAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.transactionCategorySpinner.adapter = categoryAdapter

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
                    when (position) {
                        0 -> {
                            viewModel.setOverall()
                        }
                        1 -> {
                            viewModel.setIncome()
                        }
                        2 -> {
                            viewModel.setExpense()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    viewModel.getExpenses("overall")
                }
            }

        binding.transactionCategorySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoriesFilter.add(Constants.transactionCategory[position])
                viewModel.getCategoryExpenses(categoriesFilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
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

    private fun launchSearchListener() {
        binding.searchInput.doOnTextChanged { text, start, before, count ->
            if (text.toString() == "") {
                viewModel.getExpenses("overall")
            }
            viewModel.getQueryExpenses(text.toString())
        }
    }

    private fun launchFilterListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.transactionFilter.collect { filter ->
                when (filter) {
                    "overall" -> {
                        viewModel.getExpenses("overall")
                    }
                    "income" -> {
                        viewModel.getExpenses("income")
                    }
                    "expense" -> {
                        viewModel.getExpenses("expense")
                    }
                }
            }
        }
    }

    private fun updateRv(newExpenses: List<Expense>) {
        adapter.setData(newExpenses)
    }
}