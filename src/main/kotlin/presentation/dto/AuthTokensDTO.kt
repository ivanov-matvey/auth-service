package presentation.dto

data class AuthTokensDTO(
    val accessToken: String,
    val refreshToken: String
)
