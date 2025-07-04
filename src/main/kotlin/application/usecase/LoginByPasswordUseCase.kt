package application.usecase

import application.util.redisLimiter
import domain.repository.UserRepository
import domain.service.RedisService
import domain.value.Email
import domain.value.Password
import presentation.dto.AuthTokensDTO
import shared.InvalidPasswordException
import shared.TooManyAttemptsException

class LoginByPasswordUseCase(
    private val userRepository: UserRepository,
    private val redisService: RedisService,
    private val generateLoginTokensUseCase: GenerateLoginTokensUseCase,
) {
    operator fun invoke(emailRaw: String, passwordRaw: String): AuthTokensDTO {
        val email = Email.create(emailRaw)

        val user = userRepository.findByEmail(email.toString())
            ?: throw InvalidPasswordException()

        val loginFailuresKey = "email:login-failures:$email"

        redisLimiter(
            redisService = redisService,
            key = loginFailuresKey,
            limit = 5,
            exception = { ttl -> TooManyAttemptsException(ttl) },
            ttlSeconds = 300
        )

        if (!Password.verify(passwordRaw, user.password)) {
            throw InvalidPasswordException()
        }

        redisService.del(loginFailuresKey)

        return generateLoginTokensUseCase(email.toString())
    }
}