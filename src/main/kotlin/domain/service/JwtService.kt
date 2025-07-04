package domain.service

import java.util.Date

interface JwtService {
    fun generateAccessToken(email: String): String
    fun generateRefreshToken(email: String): String
    fun validateToken(token: String): String?
    fun parseToken(token: String): ParsedJwt
}

data class ParsedJwt(
    val email: String,
    val expiration: Date
)
