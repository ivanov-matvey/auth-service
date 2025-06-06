package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class CodeVerifyRequest(
    val email: String,
    val code: String
)
