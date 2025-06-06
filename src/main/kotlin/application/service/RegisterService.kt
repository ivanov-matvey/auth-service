package application.service

import application.usecase.CodeVerifyUseCase
import application.usecase.RegisterVerifyUseCase
import shared.InvalidRegistrationTokenException
import java.util.UUID

class RegisterService(
    private val codeVerifyUseCase: CodeVerifyUseCase,
    private val registerVerifyUseCase: RegisterVerifyUseCase,
) {
    operator fun invoke(email: String, code: String): UUID {
        val verified = codeVerifyUseCase.invoke(email, code)

        if (verified) return registerVerifyUseCase(email)
        else throw InvalidRegistrationTokenException()
    }
}