package com.developer.finance.featureBillSplitting.data.remote.dto.findUserResponse

data class FindUserResponseDto(
    val success: Boolean,
    val users: List<UserDto>
)