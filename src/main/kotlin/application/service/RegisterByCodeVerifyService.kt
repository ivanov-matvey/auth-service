package application.service

import application.usecase.CheckUserUseCase
import application.usecase.VerifyCodeUseCase
import application.usecase.GenerateRegisterTokenUseCase
import presentation.dto.RegisterTokenDTO
import shared.InvalidConfirmationCodeException
import shared.UserAlreadyExistsException

class RegisterByCodeVerifyService(
    private val checkUserUseCase: CheckUserUseCase,
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val generateRegisterTokenUseCase: GenerateRegisterTokenUseCase
) {
    operator fun invoke(email: String, code: String): RegisterTokenDTO {
        val isUserExists = checkUserUseCase(email)
        if (isUserExists) throw UserAlreadyExistsException()

        val verified = verifyCodeUseCase(email, code)
        if (verified) return generateRegisterTokenUseCase(email)
        else throw InvalidConfirmationCodeException()
    }
}