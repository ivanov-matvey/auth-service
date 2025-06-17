package domain.service

interface JwtService {
    fun generateAccessToken(email: String): String
    fun generateRefreshToken(email: String): String
    fun validateToken(token: String): String?
}
