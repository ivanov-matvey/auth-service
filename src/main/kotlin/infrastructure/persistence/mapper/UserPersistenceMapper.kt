package infrastructure.persistence.mapper

import domain.model.User
import infrastructure.persistence.dao.UserDAO

fun daoToModel(dao: UserDAO): User = User (
    id = dao.id.value,
    email = dao.email,
    password = dao.password,
    fullName = dao.fullName,
    birthday = dao.birthday
)
