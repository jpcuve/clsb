package com.messio.clsb

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority


class GateAuthenticationToken(
    val p: Any,
    val c: Any,
    val email: String,
    val roles: Array<String>,
    val features: Array<String>,
    val aspects: Array<String>
): AbstractAuthenticationToken(roles.map { SimpleGrantedAuthority("ROLE_${it.uppercase()}")}){
    override fun getCredentials() = c
    override fun getPrincipal() = p
    override fun isAuthenticated() = true
}
