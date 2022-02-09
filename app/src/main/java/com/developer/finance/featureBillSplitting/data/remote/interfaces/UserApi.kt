package com.developer.finance.featureBillSplitting.data.remote.interfaces

import com.developer.finance.featureBillSplitting.data.remote.dto.LoginRegisterResponseDto
import com.developer.finance.featureBillSplitting.data.remote.dto.findUserResponse.FindUserResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @GET("find/?user_id=0")
    suspend fun getAllUsers(): FindUserResponseDto

    @GET("find/?user_id={user_id}")
    suspend fun getUserById(@Path("user_id") id: String): FindUserResponseDto

    @POST("register")
    suspend fun registerUser(
        @Body name: String,
        @Body username: String,
        @Body email: String,
        @Body password: String
    ): LoginRegisterResponseDto

    @GET("login")
    suspend fun loginUser(
        @Body username: String,
        @Body password: String
    ): LoginRegisterResponseDto
}