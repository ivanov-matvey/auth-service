package application.service

import application.usecase.GenerateAccessTokenUseCase
import application.usecase.ValidateTokenUseCase

class RefreshTokenService(
    private val validateTokenUseCase: ValidateTokenUseCase,
    private val generateAccessTokenUseCase: GenerateAccessTokenUseCase
) {
    operator fun invoke(token: String): String {
        val email = validateTokenUseCase(token)
        return generateAccessTokenUseCase(email)
    }
}