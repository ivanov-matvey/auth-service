package infrastructure.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import domain.service.JwtService
import java.util.Date

object JwtServiceImpl : JwtService {
    private val audience = System.getenv("JWT_AUDIENCE")
    private val issuer = System.getenv("JWT_ISSUER")
    private val secret = System.getenv("JWT_SECRET")
    private val ttlAccessToken = System.getenv("TTL_ACCESS_TOKEN")
    private val ttlRefreshToken = System.getenv("TTL_REFRESH_TOKEN")

    private fun generateToken(email: String, ttl: Long): String {
        return JWT
            .create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("email", email)
            .withExpiresAt(Date(
                System.currentTimeMillis() + ttl * 100
            ))
            .sign(Algorithm.HMAC256(secret))
    }

    override fun generateAccessToken(email: String): String {
        return generateToken(email, ttlAccessToken.toLong())
    }

    override fun generateRefreshToken(email: String): String {
        return generateToken(email, ttlRefreshToken.toLong())
    }

    override fun validateToken(token: String): Boolean {
        TODO("Not implemented yet.")
    }
}