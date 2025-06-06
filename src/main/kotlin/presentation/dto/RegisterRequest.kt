package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String
)
