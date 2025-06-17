package infrastructure.persistence.repository

import domain.model.User
import domain.repository.UserRepository
import infrastructure.persistence.dao.UserDAO
import infrastructure.persistence.mapper.daoToModel
import infrastructure.persistence.table.UserTable
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresUserRepositoryImpl: UserRepository {
    override fun findByEmail(email: String): User? = transaction {
        UserDAO
            .find { (UserTable.email eq email) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override fun save(user: User): User = transaction {
        daoToModel(
            UserDAO.new {
                email = user.email
                password = user.password
                fullName = user.fullName
                birthday = user.birthday
            }
        )
    }
}