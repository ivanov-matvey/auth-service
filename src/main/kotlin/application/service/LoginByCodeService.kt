package application.service

import application.usecase.CheckUserUseCase
import application.usecase.SendCodeUseCase
import domain.service.CodeType
import shared.UserNotFoundException

class LoginByCodeService(
    private val checkUserUseCase: CheckUserUseCase,
    private val sendCodeUseCase: SendCodeUseCase,
) {
    operator fun invoke(email: String) {
        val isUserExists = checkUserUseCase(email)
        if (!isUserExists) throw UserNotFoundException()

        sendCodeUseCase(email, CodeType.LOGIN)
    }
}