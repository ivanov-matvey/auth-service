package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterConfirmRequest(
    val token: String,
    val fullName: String,
    val birthday: String,
    val password: String
)
