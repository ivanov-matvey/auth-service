package application.usecase

import application.util.redisLimiter
import domain.repository.UserRepository
import domain.service.RedisService
import org.mindrot.jbcrypt.BCrypt
import presentation.dto.AuthTokensDTO
import shared.InvalidPasswordException
import shared.TooManyAttemptsException

class LoginByPasswordUseCase(
    private val userRepository: UserRepository,
    private val redisService: RedisService,
    private val generateAuthTokensUseCase: GenerateAuthTokensUseCase,
) {
    operator fun invoke(email: String, password: String): AuthTokensDTO {
        val user = userRepository.findByEmail(email)!!

        val loginFailuresKey = "email:login-failures:$email"

        redisLimiter(
            redisService = redisService,
            key = loginFailuresKey,
            limit = 5,
            exception = { ttl -> TooManyAttemptsException(ttl) },
            ttlSeconds = 300
        )

        if (!BCrypt.checkpw(password, user.password))
            throw InvalidPasswordException()

        redisService.del(loginFailuresKey)

        return generateAuthTokensUseCase(email)
    }
}