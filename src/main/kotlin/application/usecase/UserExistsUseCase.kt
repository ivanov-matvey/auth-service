package application.usecase

import domain.repository.UserRepository
import shared.UserAlreadyExistsException

class UserExistsUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(email: String) {
        val user = userRepository.findByEmail(email)
        if (user != null) throw UserAlreadyExistsException()
    }
}