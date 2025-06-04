package infrastructure.persistence.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

object UserTable : UUIDTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val fullName = varchar("full_name", 255)
    val birthday = date("birthday")
}
