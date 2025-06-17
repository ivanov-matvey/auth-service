package application.usecase

import domain.service.JwtService
import shared.InvalidRefreshTokenException

class ValidateTokenUseCase(
    private val jwtService: JwtService,
) {
    operator fun invoke(token: String): String {
        val email = jwtService.validateToken(token)
        if (email == null)
            throw InvalidRefreshTokenException()
        else return email
    }
}