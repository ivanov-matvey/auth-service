package infrastructure.persistence.repository

import domain.model.User
import domain.repository.UserRepository

class PostgresUserRepository: UserRepository {
    override fun findByEmail(email: String): User? {
        TODO("Not yet implemented")
    }
}