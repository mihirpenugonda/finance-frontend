package com.developer.finance.featureBillSplitting.data.remote.dto.findUserResponse

import com.developer.finance.featureBillSplitting.data.local.User

data class UserDto(
    val __v: Int,
    val _id: String,
    val avatar: AvatarDto,
    val email: String,
    val joined_date: String,
    val name: String,
    val role: String,
    val username: String
) {
    fun toUserModel(): User {
        return User(
            email,
            joined_date,
            name,
            username,
            avatar.url
        )
    }
}