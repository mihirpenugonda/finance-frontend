package com.developer.finance.featureBillSplitting.presentation.friendsFragment

import android.content.Intent
import android.content.SharedPreferences
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
import com.developer.finance.common.addTransaction.AddTransactionActivity
import com.developer.finance.common.base.BaseFragment
import com.developer.finance.databinding.FragmentFriendsBinding
import com.developer.finance.featureBillSplitting.domain.usecases.UserUsecases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class FriendsFragment : BaseFragment<FragmentFriendsBinding>() {

    val viewModel: FriendsViewModel by activityViewModels<FriendsViewModel>()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var userUsecases: UserUsecases

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.mainSwipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launchWhenStarted {
                userUsecases.getAllUsersUsecase().collect {
                    Log.d("GetAllUsers: ", it.toString())
                }
                userUsecases.loginUsecase("amitapenugonda", "amitape").collect {
                    Log.d("LoginUsers: ", it.toString())
                    sharedPreferences.getString("token", "doesnt work")
                        ?.let { it1 -> Log.d("LoginUsers: ", it1) }
                }
            }
            binding.mainSwipeRefreshLayout.isRefreshing = false
        }

        binding.addExpenseButton.setOnClickListener {
            val intent = Intent(activity, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFriendsBinding =
        FragmentFriendsBinding.inflate(inflater, container, false)

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(context, R.anim.empty_anim)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.empty_anim)
        }
    }
}