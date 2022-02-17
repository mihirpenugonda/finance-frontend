package com.developer.finance.featureBillSplitting.data.remote.interfaces

import com.developer.finance.featureBillSplitting.data.remote.dto.LoginRegisterResponseDto
import com.developer.finance.featureBillSplitting.data.remote.dto.findUserResponse.FindUserResponseDto
import retrofit2.http.*

interface UserApi {
    @GET("find/?user_id=0")
    suspend fun getAllUsers(): FindUserResponseDto

    @GET("find/?user_id={user_id}")
    suspend fun getUserById(@Path("user_id") id: String): FindUserResponseDto

    @POST("register")
    suspend fun registerUser(
        @Body registerBodyItem: HashMap<String, String>
    ): LoginRegisterResponseDto

    @PUT("login")
    suspend fun loginUser(
        @Body loginBodyItem: HashMap<String, String>
    ): LoginRegisterResponseDto
}