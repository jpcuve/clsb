package com.messio.clsb

data class AuthorizationToken(
    val validity: Long = 0L,
    val clientId: String = "",
    val email: String = "",
    val redirectUri: String = "",
    val scope: String = "",
)

data class AccessToken(
    val validity: Long = 0L,
    val clientId: String = "",
    val scope: String = "",
)

data class ClientOutline(
    val id: Long = 0L,
    val identifier: String = "",
    val name: String = ""
)

data class AccountOutline(
    val id: Long = 0L,
    val name: String = "",
)

data class PasswordChange(
    val token: String = "",
    val current: String = "",
    val password: String = "",
)

data class PasswordReset(
    val email: String = "",
)
