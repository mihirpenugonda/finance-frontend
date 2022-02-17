package com.developer.finance.featureBillSplitting.domain.usecases.users

import android.util.Log
import com.developer.finance.featureBillSplitting.data.local.User
import com.developer.finance.featureBillSplitting.data.remote.UserApiRepository
import com.developer.finance.featureBillSplitting.data.remote.dto.findUserResponse.toUserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllUsersUsecase @Inject constructor(
    private val userApiRepository: UserApiRepository
) {
    operator fun invoke(): Flow<List<User>> = flow {
        try {
            val coins = userApiRepository.getAllUsers()
            Log.d("UserApiRepo All Users: ", coins.toString())
            coins.users?.let { emit(it.map { it.toUserModel() }) }
        } catch (e: IOException) {
            Log.d("getallusers usecase:", e.toString())
        } catch (e: HttpException) {
            Log.d("getallusers usecase:", "device not connected to internet")
        }
    }
}