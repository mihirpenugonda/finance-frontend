package com.developer.finance.presentation.expensesFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.developer.finance.R
import com.developer.finance.common.base.BaseFragment
import com.developer.finance.data.local.entity.Expense
import com.developer.finance.databinding.FragmentExpensesBinding
import kotlinx.coroutines.flow.collect


class ExpensesFragment : BaseFragment<FragmentExpensesBinding, ExpensesViewModel>() {

    override val viewModel: ExpensesViewModel by activityViewModels<ExpensesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        launchExpenseListener()
        launchFilterListener()
    }

    private fun initViews() {
        binding.addExpenseButton.setOnClickListener {
            findNavController().navigate(R.id.add_transaction_activity)
        }
        val typeAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.types,
            android.R.layout.simple_spinner_item
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.typeSelector.adapter = typeAdapter
        binding.typeSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        viewModel.setOverall()
                        binding.typeSelectorText.text = getString(R.string.text_all_transactions)
                    }
                    1 -> {
                        viewModel.setIncome()
                        binding.typeSelectorText.text = getString(R.string.text_income)
                    }
                    2 -> {
                        viewModel.setExpense()
                        binding.typeSelectorText.text = getString(R.string.text_expense)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.setOverall()
            }
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

    private fun launchExpenseListener() =
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { event ->
                when (event) {
                    is ExpensesFragmentEvent.Empty -> {

                    }
                    is ExpensesFragmentEvent.Success -> {
                        Log.d("Updated", event.expenses.size.toString())
                        calculateTotalIncomeExpense(event.expenses)
                    }
                    else -> {}
                }
            }
        }

    @SuppressLint("SetTextI18n")
    private fun calculateTotalIncomeExpense(expenses: List<Expense>) {
        val (totalIncome, totalExpense) = expenses.partition { it.type == "income" }
        val income = totalIncome.sumOf { Integer.parseInt(it.amount) }
        val expense = totalExpense.sumOf { Integer.parseInt(it.amount) }

        var balance = income - expense

        binding.totalBalanceCardView.textTotalBalanceAmount.text = if(balance < 0) {
            balance *= -1
            "-₹$balance"
        } else "+₹$balance"
        binding.totalIncomeCardView.textTotalIncomeAmount.text = "+₹$income"
        binding.totalExpenseCardView.textTotalExpenseAmount.text = "-₹$expense"

    }


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExpensesBinding = FragmentExpensesBinding.inflate(inflater, container, false)

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(context, R.anim.empty_anim)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.empty_anim)
        }
    }
}