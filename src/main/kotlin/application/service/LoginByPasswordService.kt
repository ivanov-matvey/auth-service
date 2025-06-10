package application.service

import application.usecase.CheckUserUseCase
import application.usecase.LoginByPasswordUseCase
import presentation.dto.AuthTokensDTO
import shared.UserNotFoundException

class LoginByPasswordService(
    private val checkUserUseCase: CheckUserUseCase,
    private val loginByPasswordUseCase: LoginByPasswordUseCase
) {
    operator fun invoke(email: String, password: String): AuthTokensDTO {
        val isUserExists = checkUserUseCase(email)
        if (!isUserExists) throw UserNotFoundException()

        return loginByPasswordUseCase(email, password)
    }
}