package com.developer.finance.featureBillSplitting.domain.usecases.users

import android.content.SharedPreferences
import android.util.Log
import com.developer.finance.featureBillSplitting.data.remote.UserApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RegisterUsecase @Inject constructor(
    private val userApiRepository: UserApiRepository,
    private val sharedPreferences: SharedPreferences
) {

    operator fun invoke(
        name: String,
        username: String,
        password: String,
        email: String
    ): Flow<Boolean> = flow {
        try {
            val response = userApiRepository.registerUser(name, username, email, password)
            sharedPreferences.edit().putString("token", response.token).apply()
            emit(true)
        } catch (e: HttpException) {
            Log.d("register usecase:", "device not connected to internet")
            emit(false)
        } catch (e: IOException) {
            Log.d("register usecase:", e.toString())
            emit(false)
        }
    }

}