package com.developer.finance.featureBillSplitting.presentation.addSplits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.developer.finance.common.addTransaction.AddTransactionViewModel
import com.developer.finance.common.base.BaseFragment
import com.developer.finance.databinding.FragmentAddSplitsTabBinding
import com.pusher.client.Pusher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddSplitsFragment : BaseFragment<FragmentAddSplitsTabBinding>() {
    val viewModel: AddTransactionViewModel by activityViewModels<AddTransactionViewModel>()

    @Inject
    lateinit var pusherInstance: Pusher

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddSplitsTabBinding = FragmentAddSplitsTabBinding.inflate(inflater, container, false)

}