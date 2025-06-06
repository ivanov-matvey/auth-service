package infrastructure.persistence.dao

import infrastructure.persistence.table.UserTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class UserDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserDAO>(UserTable)

    var email by UserTable.email
    var password by UserTable.password
    var fullName by UserTable.fullName
    var birthday by UserTable.birthday
}