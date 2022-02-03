package com.developer.finance.presentation.addTransaction.addSplits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.developer.finance.common.base.BaseFragment
import com.developer.finance.databinding.FragmentAddSplitsTabBinding
import com.developer.finance.presentation.addTransaction.AddTransactionViewModel

class AddSplitsFragment : BaseFragment<FragmentAddSplitsTabBinding>() {
    val viewModel: AddTransactionViewModel by activityViewModels<AddTransactionViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddSplitsTabBinding = FragmentAddSplitsTabBinding.inflate(inflater, container, false)

}