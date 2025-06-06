package presentation.dto

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)
