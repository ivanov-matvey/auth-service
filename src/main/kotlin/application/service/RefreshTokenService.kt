package application.service

import application.usecase.RefreshTokenUseCase
import presentation.dto.AuthTokensDTO

class RefreshTokenService(
    private val refreshTokenUseCase: RefreshTokenUseCase
) {
    operator fun invoke(refreshToken: String?): AuthTokensDTO {
        return refreshTokenUseCase(refreshToken)
    }
}

