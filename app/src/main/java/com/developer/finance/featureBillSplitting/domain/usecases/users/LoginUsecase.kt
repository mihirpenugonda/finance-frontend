package com.developer.finance.featureBillSplitting.domain.usecases.users

import android.content.SharedPreferences
import android.util.Log
import com.developer.finance.featureBillSplitting.data.remote.UserApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUsecase @Inject constructor(
    private val userApiRepository: UserApiRepository,
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(username: String, password: String): Flow<Boolean> = flow {
        try {
            val response = userApiRepository.loginUser(username, password)
            Log.d("login usecase: ", response.toString())
            sharedPreferences.edit().putString("token", response.token).apply()
            emit(true)
        } catch (e: HttpException) {
            Log.d("login usecase:", "device not connected to internet")
            emit(false)
        } catch (e: IOException) {
            Log.d("login usecase:", e.toString())
            emit(false)
        }
    }
}