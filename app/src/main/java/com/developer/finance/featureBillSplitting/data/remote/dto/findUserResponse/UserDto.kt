package com.developer.finance.featureBillSplitting.data.remote.dto.findUserResponse

import com.developer.finance.featureBillSplitting.data.local.User

data class UserDto(
    val __v: Int,
    val _id: String,
    val avatar: AvatarDto,
    val email: String,
    val joining_date: String,
    val name: String,
    val role: String,
    val username: String
)

fun UserDto.toUserModel(): User {
    return User(
        email,
        joining_date,
        name,
        username,
    )
}
