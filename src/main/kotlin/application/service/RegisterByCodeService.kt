package application.service

import application.usecase.CheckUserUseCase
import application.usecase.SendCodeUseCase
import domain.service.CodeType
import shared.UserAlreadyExistsException

class RegisterByCodeService(
    private val checkUserUseCase: CheckUserUseCase,
    private val sendCodeUseCase: SendCodeUseCase,
) {
    operator fun invoke(email: String) {
        val isUserExists = checkUserUseCase(email)
        if (isUserExists) throw UserAlreadyExistsException()

        sendCodeUseCase(email, CodeType.REGISTER)
    }
}