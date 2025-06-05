package presentation.dto

import infrastructure.util.UUIDSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserDTO (
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email: String,
    val fullName: String,
    val birthday: LocalDate
)
