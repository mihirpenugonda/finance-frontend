package com.developer.finance.featureBillSplitting.data.remote

import com.developer.finance.featureBillSplitting.data.remote.dto.LoginRegisterResponseDto
import com.developer.finance.featureBillSplitting.data.remote.dto.findUserResponse.FindUserResponseDto

interface UserApiRepository {

    suspend fun getAllUsers(): FindUserResponseDto

    suspend fun getUser(id: String): FindUserResponseDto

    suspend fun loginUser(username: String, password: String): LoginRegisterResponseDto

    suspend fun registerUser(
        name: String,
        username: String,
        email: String,
        password: String
    ): LoginRegisterResponseDto

}