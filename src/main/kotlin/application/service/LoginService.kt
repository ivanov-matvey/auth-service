package application.service

import application.usecase.CodeVerifyUseCase
import application.usecase.LoginByCodeUseCase
import presentation.dto.AuthTokens
import shared.InvalidConfirmationCodeException

class LoginService(
    private val codeVerifyUseCase: CodeVerifyUseCase,
    private val loginByCodeUseCase: LoginByCodeUseCase,
) {
    fun getTokensByCode(email: String, code: String): AuthTokens {
        val verified = codeVerifyUseCase(email, code)

        if (verified) return loginByCodeUseCase(email)
        else throw InvalidConfirmationCodeException()
    }
}