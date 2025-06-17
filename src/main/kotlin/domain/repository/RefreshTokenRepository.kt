package domain.repository

interface RefreshTokenRepository {
    fun findUserByToken(token: String): String?
    fun save(token: String, email: String)
}