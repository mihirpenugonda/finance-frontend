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

class GetUserByIdUsecase @Inject constructor(
    private val userApiRepository: UserApiRepository
) {
    operator fun invoke(id: String): Flow<User> = flow {
        try {
            val response = userApiRepository.getUser(id)
            Log.d("finduserbyid usecase: ", response.toString())
            response.users?.map { it.toUserModel() }?.let { emit(it[0]) }
        } catch (e: IOException) {
            Log.d("finduserbyid usecase: ", e.toString())
        } catch (e: HttpException) {
            Log.d("finduserbyid usecase: ", "device not connected to internet")
        }
    }
}