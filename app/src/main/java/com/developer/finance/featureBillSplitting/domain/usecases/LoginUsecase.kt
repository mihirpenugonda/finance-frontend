package com.developer.finance.featureBillSplitting.domain.usecases

import android.content.SharedPreferences
import android.util.Log
import com.developer.finance.featureBillSplitting.data.remote.interfaces.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUsecase {

    @Inject
    lateinit var userApi: UserApi

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    suspend operator fun invoke(username: String, password: String): Unit =
        withContext(Dispatchers.IO) {
            val response = try {
                userApi.loginUser(username, password)
            } catch (e: IOException) {
                Log.d("login usecase: ", e.toString())
                return@withContext
            } catch (e: HttpException) {
                Log.d("login usecase:", "device not connected to internet")
                return@withContext
            }

            if (response.isSuccessful) {
                sharedPreferences.edit().putString("token", response.body()?.token).apply()
                true
            }
        }
}