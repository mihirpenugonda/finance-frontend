package com.developer.finance.featureBillSplitting.presentation.friendsFragment

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
import com.developer.finance.common.base.BaseFragment
import com.developer.finance.databinding.FragmentFriendsBinding
import com.developer.finance.featureBillSplitting.data.remote.interfaces.UserApi
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class FriendsFragment : BaseFragment<FragmentFriendsBinding>() {

    val viewModel: FriendsViewModel by activityViewModels<FriendsViewModel>()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit =
            Retrofit.Builder().baseUrl("http://192.168.0.32:3000/api/users/").addConverterFactory(
                GsonConverterFactory.create()
            ).build().create(UserApi::class.java)

//        Log.d("TESTING: ", GetAllUsersUsecase().invokeExp().toString())


        lifecycleScope.launchWhenStarted {
            val response = try {
                retrofit.getAllUsers()
            } catch (e: IOException) {
                println(e)
                return@launchWhenStarted
            } catch (e: HttpException) {
                println("Error HTTP")
                return@launchWhenStarted
            }

            Log.d("response", response.body().toString())


            Log.d(
                "SharedPreferences",
                sharedPreferences.getString("token", "loginException").toString()
            )
            sharedPreferences.edit().putString("token", "hereitworks").apply()
            Log.d(
                "SharedPreferences",
                sharedPreferences.getString("token", "loginException").toString()
            )

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