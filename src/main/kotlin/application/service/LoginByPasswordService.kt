package application.service

import application.usecase.CheckUserUseCase
import application.usecase.LoginByPasswordUseCase
import shared.UserNotFoundException

class LoginByPasswordService(
    private val checkUserUseCase: CheckUserUseCase,
    private val loginByPasswordUseCase: LoginByPasswordUseCase
) {
    operator fun invoke(email: String, password: String) {
        val isUserExists = checkUserUseCase(email)
        if (!isUserExists) throw UserNotFoundException()

        loginByPasswordUseCase(email, password)
    }
}