package application.service

import application.usecase.LogoutUseCase

class LogoutService(
    private val logoutUseCase: LogoutUseCase
) {
    operator fun invoke(accessToken: String?, refreshToken: String?) {
        logoutUseCase(accessToken, refreshToken)
    }
}
