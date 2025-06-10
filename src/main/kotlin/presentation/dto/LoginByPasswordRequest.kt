package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginByPasswordRequest(
    val email: String,
    val password: String
)
