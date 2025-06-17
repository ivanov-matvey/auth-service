package application.usecase

import domain.service.JwtService

class GenerateAccessTokenUseCase(
    private val jwtService: JwtService,
) {
    operator fun invoke(email: String): String {
        return jwtService.generateAccessToken(email)
    }
}