package com.developer.finance.featureExpenseManagement.presentation.transactionDashboardFragment

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.developer.finance.databinding.FragmentExpensesDashboardBinding
import com.developer.finance.featureExpenseManagement.data.local.entity.Transaction
import com.developer.finance.featureExpenseManagement.presentation.ExpenseViewModel
import kotlinx.coroutines.flow.collect


class TransactionDashboardFragment : BaseFragment<FragmentExpensesDashboardBinding>() {

    val viewModel: ExpenseViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDashboardExpenses("overall")
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
                        viewModel.setFilter("overall")
                        overallMode()
                    }
                    1 -> {
                        viewModel.setFilter("income")
                        incomeMode()
                    }
                    2 -> {
                        viewModel.setFilter("expense")
                        expenseMode()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.getDashboardExpenses("overall")
            }
        }

        binding.viewAllTransactions.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_expenses_to_transactionActivity)
        }

    }

    private fun launchFilterListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.dashboardFilter.collect { filter ->
                viewModel.getDashboardExpenses(filter)
            }
        }
    }

    private fun launchExpenseListener() =
        lifecycleScope.launchWhenStarted {
            viewModel.dashboardState.collect { event ->
                when (event) {
                    is TransactionDashboardFragmentEvent.Success -> {
                        calculateTotalIncomeExpense(event.expenses)
                        displayMode()
                    }
                    is TransactionDashboardFragmentEvent.Empty -> {
                        binding.totalBalanceCardView.textTotalBalanceAmount.text = "0"
                        binding.totalExpenseCardView.textTotalExpenseAmount.text = "0"
                        binding.totalIncomeCardView.textTotalIncomeAmount.text = "0"
                    }
                    else -> {}
                }
            }
        }

    @SuppressLint("SetTextI18n")
    private fun calculateTotalIncomeExpense(expenses: List<Transaction>) {
        val (totalIncome, totalExpense) = expenses.partition { it.type == "income" }
        val income = totalIncome.sumOf { Integer.parseInt(it.amount) }
        val expense = totalExpense.sumOf { Integer.parseInt(it.amount) }

        var balance = income - expense

        binding.totalBalanceCardView.textTotalBalanceAmount.text = if (balance < 0) {
            balance *= -1; "-₹$balance"
        } else "+₹$balance"
        binding.totalIncomeCardView.textTotalIncomeAmount.text =
            if (viewModel.dashboardFilter.value == "expense") {
                "0"
            } else {
                "+₹$income"
            }
        binding.totalExpenseCardView.textTotalExpenseAmount.text =
            if (viewModel.dashboardFilter.value == "income") {
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
    ): FragmentExpensesDashboardBinding =
        FragmentExpensesDashboardBinding.inflate(inflater, container, false)

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(context, R.anim.empty_anim)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.empty_anim)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDashboardExpenses("overall")
    }
}