package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterVerifyRequest(
    val email: String,
    val code: String
)
