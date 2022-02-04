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


class ExpensesFragment : BaseFragment<FragmentExpensesBinding>() {

    val viewModel: ExpensesViewModel by activityViewModels<ExpensesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
//        initRv()
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
                        overallMode()
                    }
                    1 -> {
                        viewModel.setIncome()
                        incomeMode()
                    }
                    2 -> {
                        viewModel.setExpense()
                        expenseMode()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.setOverall()
            }
        }

        binding.viewAllTransactions.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_expenses_to_transactionActivity)
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
                        emptyMode()
                    }
                    is ExpensesFragmentEvent.Success -> {
                        Log.d("Updated", event.expenses.size.toString())
                        calculateTotalIncomeExpense(event.expenses)
                        displayMode()
//                        updateRv(event.expenses)
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

        binding.totalBalanceCardView.textTotalBalanceAmount.text = if (balance < 0) {
            balance *= -1; "-₹$balance"
        } else "+₹$balance"
        binding.totalIncomeCardView.textTotalIncomeAmount.text =
            if (viewModel.transactionFilter.value == "expense") {
                "0"
            } else {
                "+₹$income"
            }
        binding.totalExpenseCardView.textTotalExpenseAmount.text =
            if (viewModel.transactionFilter.value == "income") {
                "0"
            } else {
                "-₹$expense"
            }
    }

    private fun overallMode() = with(binding) {
        totalBalanceCardView.totalBalanceCardView.animate().alpha(1f)
        totalIncomeCardView.totalIncomeCardView.visibility = View.VISIBLE
        totalBalanceCardView.totalBalanceCardView.visibility = View.VISIBLE
        totalExpenseCardView.totalExpenseCardView.visibility = View.VISIBLE
        expenseIncomeLayoutSpacer.visibility = View.VISIBLE
    }

    private fun expenseMode() = with(binding) {
        totalBalanceCardView.totalBalanceCardView.animate().alpha(0.0f)
        totalIncomeCardView.totalIncomeCardView.visibility = View.GONE
        totalBalanceCardView.totalBalanceCardView.visibility = View.GONE
        totalExpenseCardView.totalExpenseCardView.visibility = View.VISIBLE
        expenseIncomeLayoutSpacer.visibility = View.GONE
    }

    private fun incomeMode() = with(binding) {
        totalBalanceCardView.totalBalanceCardView.animate().alpha(0.0f)
        totalExpenseCardView.totalExpenseCardView.visibility = View.GONE
        totalBalanceCardView.totalBalanceCardView.visibility = View.GONE
        totalIncomeCardView.totalIncomeCardView.visibility = View.VISIBLE
        expenseIncomeLayoutSpacer.visibility = View.GONE
    }

    private fun emptyMode() = with(binding) {
        expensesFragmentScrollView.visibility = View.GONE
        expensesFragmentEmptyLayout.visibility = View.VISIBLE
    }

    private fun displayMode() = with(binding) {
        expensesFragmentScrollView.visibility = View.VISIBLE
        expensesFragmentEmptyLayout.visibility = View.GONE
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