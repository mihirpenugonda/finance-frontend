package com.developer.finance.presentation.friendsFragment

import android.os.Bundle
import android.view.LayoutInflater
 import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.developer.finance.R
import com.developer.finance.common.base.BaseFragment
import com.developer.finance.databinding.FragmentFriendsBinding

class FriendsFragment : BaseFragment<FragmentFriendsBinding>() {

    val viewModel: FriendsViewModel by activityViewModels<FriendsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addSplitButton.setOnClickListener {
            findNavController().navigate(R.id.add_transaction_activity)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFriendsBinding =
        FragmentFriendsBinding.inflate(inflater, container, false)

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if(enter) {
            AnimationUtils.loadAnimation(context, R.anim.empty_anim)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.empty_anim)
        }
    }
}