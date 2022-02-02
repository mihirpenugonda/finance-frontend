package com.developer.finance.presentation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.developer.finance.R
import com.developer.finance.common.UIModeImpl
import com.developer.finance.databinding.ActivityMainBinding
import com.developer.finance.presentation.expensesFragment.ExpensesViewModel
import com.developer.finance.presentation.settingsFragment.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var themeManager: UIModeImpl
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val expensesViewModel: ExpensesViewModel by viewModels()


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        expensesViewModel

        val navController = findNavController(R.id.fragment)
        binding.bottomNavigationView.addExpenseButton.setOnClickListener {
            navController.navigate(R.id.add_transaction_activity)
        }
        binding.bottomNavigationView.bottomMenu.setupWithNavController(navController)

        observeThemeMode()
    }

    private fun observeThemeMode() {

        lifecycleScope.launchWhenCreated {
            settingsViewModel.getUIMode.collect {
                val mode = when (it) {
                    true -> AppCompatDelegate.MODE_NIGHT_YES
                    false -> AppCompatDelegate.MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }

}