package application.usecase

import domain.repository.UserRepository
import domain.service.EmailValidationService
import shared.InvalidEmailException

class CheckUserUseCase(
    private val userRepository: UserRepository,
    private val emailValidationService: EmailValidationService
) {
    operator fun invoke(email: String): Boolean {
        if (!emailValidationService.isValid(email)) {
            throw InvalidEmailException()
        }

        val isUserExists = userRepository.findByEmail(email)

        return isUserExists != null
    }
}