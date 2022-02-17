package com.developer.finance.featureBillSplitting.domain.usecases

import com.developer.finance.featureBillSplitting.domain.usecases.users.GetAllUsersUsecase
import com.developer.finance.featureBillSplitting.domain.usecases.users.GetUserByIdUsecase
import com.developer.finance.featureBillSplitting.domain.usecases.users.LoginUsecase
import com.developer.finance.featureBillSplitting.domain.usecases.users.RegisterUsecase

data class UserUsecases(
    val getAllUsersUsecase: GetAllUsersUsecase,
    val getUserByIdUsecase: GetUserByIdUsecase,
    val loginUsecase: LoginUsecase,
    val registerUsecase: RegisterUsecase
)