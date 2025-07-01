package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterVerifyResponse (
    val token: String
)