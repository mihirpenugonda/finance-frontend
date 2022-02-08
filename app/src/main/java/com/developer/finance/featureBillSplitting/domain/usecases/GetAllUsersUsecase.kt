package com.developer.finance.featureBillSplitting.domain.usecases

import android.util.Log
import com.developer.finance.featureBillSplitting.data.remote.interfaces.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllUsersUsecase {
    @Inject
    lateinit var userApi: UserApi

    fun invokeExp() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = try {
                userApi.getAllUsers()
            } catch (e: IOException) {
                Log.d("getallusers Usecase:", e.toString())
                return@launch
            } catch (e: HttpException) {
                Log.d("getallusers usecase:", "device not connected to internet")
                return@launch
            }

            if (response.isSuccessful) {
                response.body()
            }
        }
    }
}