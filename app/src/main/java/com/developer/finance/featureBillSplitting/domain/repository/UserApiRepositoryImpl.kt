package com.developer.finance.featureBillSplitting.domain.repository

import com.developer.finance.featureBillSplitting.data.remote.UserApiRepository
import com.developer.finance.featureBillSplitting.data.remote.dto.LoginRegisterResponseDto
import com.developer.finance.featureBillSplitting.data.remote.dto.findUserResponse.FindUserResponseDto
import com.developer.finance.featureBillSplitting.data.remote.interfaces.UserApi
import javax.inject.Inject

class UserApiRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserApiRepository {
    override suspend fun getAllUsers(): FindUserResponseDto {
        return userApi.getAllUsers()
    }

    override suspend fun getUser(id: String): FindUserResponseDto {
        return userApi.getUserById(id)
    }

    override suspend fun loginUser(username: String, password: String): LoginRegisterResponseDto {
        return userApi.loginUser(username, password)
    }

    override suspend fun registerUser(
        name: String,
        username: String,
        email: String,
        password: String
    ): LoginRegisterResponseDto {
        return userApi.registerUser(name, username, email, password)
    }
}