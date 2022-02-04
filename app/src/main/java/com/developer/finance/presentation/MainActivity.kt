package com.developer.finance.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.developer.finance.R
import com.developer.finance.databinding.ActivityMainBinding
import com.developer.finance.presentation.expensesFragment.ExpensesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val expensesViewModel: ExpensesViewModel by viewModels()


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        expensesViewModel

        val navController = findNavController(R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        observeThemeMode()
    }

    private fun observeThemeMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

}