package domain.repository

import domain.model.User

interface UserRepository {
    fun findByEmail(email: String): User?

    fun save(user: User): User
}