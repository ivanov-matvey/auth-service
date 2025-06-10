package application.service

import application.usecase.CheckUserUseCase
import application.usecase.VerifyCodeUseCase
import application.usecase.GenerateAuthTokensUseCase
import presentation.dto.AuthTokens
import shared.InvalidConfirmationCodeException
import shared.UserNotFoundException

class LoginByCodeVerifyService(
    private val checkUserUseCase: CheckUserUseCase,
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val generateAuthTokensUseCase: GenerateAuthTokensUseCase,
) {
    operator fun invoke(email: String, code: String): AuthTokens {
        val isUserExists = checkUserUseCase(email)
        if (!isUserExists) throw UserNotFoundException()

        val verified = verifyCodeUseCase(email, code)
        if (verified) return generateAuthTokensUseCase(email)
        else throw InvalidConfirmationCodeException()
    }
}