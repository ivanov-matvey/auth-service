package domain.model

import kotlinx.datetime.LocalDate
import java.util.UUID

class User(
    val id: UUID,
    val email: String,
    val password: String,
    val fullName: String,
    val birthday: LocalDate
)
