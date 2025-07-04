package application.usecase

import domain.repository.UserRepository
import domain.value.Email

class CheckUserUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(emailRaw: String): Boolean {
        val email = Email.create(emailRaw)

        val isUserExists = userRepository.findByEmail(email.toString())

        return isUserExists != null
    }
}