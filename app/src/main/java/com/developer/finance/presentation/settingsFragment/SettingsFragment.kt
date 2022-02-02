package com.developer.finance.presentation.settingsFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.developer.finance.R
import com.developer.finance.common.base.BaseFragment
import com.developer.finance.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch
import kotlin.math.log

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {
    override val viewModel: SettingsViewModel by activityViewModels<SettingsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        print(viewModel.isDarkModeSelected())

        lifecycleScope.launch {
            binding.darkModeSetting.isChecked = viewModel.isDarkModeSelected()
        }

        binding.darkModeSetting.setOnClickListener {
            val isChecked = binding.darkModeSetting.isChecked
            if(isChecked) {
                viewModel.setUIMode(true)
            }
            else {
                viewModel.setUIMode(false)
            }
        }

        binding.deleteAll.setOnClickListener {
                viewModel.deleteAllExpenses()


        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater,container,false)

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if(enter) {
            AnimationUtils.loadAnimation(context, R.anim.empty_anim)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.empty_anim)
        }
    }
}