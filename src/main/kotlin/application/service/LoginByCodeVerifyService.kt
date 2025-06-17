package application.service

import application.usecase.CheckUserUseCase
import application.usecase.VerifyCodeUseCase
import application.usecase.GenerateLoginTokensUseCase
import presentation.dto.AuthTokensDTO
import shared.InvalidConfirmationCodeException
import shared.UserNotFoundException

class LoginByCodeVerifyService(
    private val checkUserUseCase: CheckUserUseCase,
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val generateLoginTokensUseCase: GenerateLoginTokensUseCase,
) {
    operator fun invoke(email: String, code: String): AuthTokensDTO {
        val isUserExists = checkUserUseCase(email)
        if (!isUserExists) throw UserNotFoundException()

        val verified = verifyCodeUseCase(email, code)
        if (verified) return generateLoginTokensUseCase(email)
        else throw InvalidConfirmationCodeException()
    }
}