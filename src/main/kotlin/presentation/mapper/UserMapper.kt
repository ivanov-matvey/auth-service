package presentation.mapper

import domain.model.User
import presentation.dto.UserDTO

fun modelToDto(model: User): UserDTO = UserDTO(
    id = model.id,
    email = model.email,
    fullName = model.fullName,
    birthday = model.birthday
)
