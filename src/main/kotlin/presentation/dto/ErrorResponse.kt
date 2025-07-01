package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val title: String,
    val status: Int,
    val errors: Map<String, String>? = null
)
