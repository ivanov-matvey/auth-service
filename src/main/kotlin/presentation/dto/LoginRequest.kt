package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String
)
