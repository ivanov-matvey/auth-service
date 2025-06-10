package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginByCodeRequest(
    val email: String
)
