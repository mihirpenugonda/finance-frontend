package com.developer.finance.featureBillSplitting.data.remote.dto

data class LoginRegisterResponseDto(
    val id: String,
    val success: Boolean,
    val token: String
)