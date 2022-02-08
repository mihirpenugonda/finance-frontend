package com.developer.finance.common.addTransaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.developer.finance.R
import com.developer.finance.databinding.ActivityAddTransactionBinding
import com.developer.finance.featureBillSplitting.presentation.addSplits.AddSplitsFragment
import com.developer.finance.featureExpenseManagement.presentation.addExpense.AddExpenseFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTransactionActivity: AppCompatActivity() {

    lateinit var binding: ActivityAddTransactionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.expenseFragmentClose.setOnClickListener {
            finish()
        }

        val tabViewPager = AddTransactionViewPagerAdapter(supportFragmentManager, lifecycle)
        tabViewPager.addFragment(AddExpenseFragment(), "add expense")
        tabViewPager.addFragment(AddSplitsFragment(), "add split")
        binding.selectTypeViewPager.adapter = tabViewPager

        TabLayoutMediator(binding.selectTypeTabLayout, binding.selectTypeViewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "expenses"
                1 -> tab.text = "split"
            }
        }.attach()
    }
}