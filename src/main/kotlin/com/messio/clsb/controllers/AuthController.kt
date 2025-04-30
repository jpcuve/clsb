package com.messio.clsb.controllers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.KeyPair
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Date
import java.util.UUID

@RestController
@RequestMapping("/auth")
class AuthController(
    val objectMapper: ObjectMapper,
    val keyPair: KeyPair,
    @Value("\${app.identity.client-id}") val clientId: String,
) {

    @GetMapping("/sign-in")
    fun getSignIn(
        @RequestParam("response_type") responseType: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("redirect_uri") redirectUri: String,
        @RequestParam("scope") scope: String,
        res: HttpServletResponse
    ) {
        res.sendRedirect("$redirectUri?code=$CODE")
    }

    @PostMapping("/auth/token", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun postAuthToken(
        @RequestBody value: MultiValueMap<String, String>
    ): Map<String, String> {
        return (keyPair.private as RSAPrivateKey?)?.let { privateKey ->
            val algorithm = Algorithm.RSA256(keyPair.public as RSAPublicKey, privateKey)
            val idToken = JWT.create()
                .withSubject("0")  // account id
                .withAudience(clientId)
                .withExpiresAt(Date(System.currentTimeMillis() + 30 * 1000L)) // 30 seconds
                .withIssuedAt(Date(System.currentTimeMillis() - 300 * 1000L))
                .withClaim("jti", UUID.randomUUID().toString())  // unique ID
                .withClaim("nonce", "")
                .withClaim("email", "jpc@messio.com")
                .withArrayClaim("roles", arrayOf("role1", "role2"))
                .withArrayClaim("features", arrayOf("feature1", "feature2"))
                .withArrayClaim("aspects", arrayOf("aspect1", "aspect2"))
                .sign(algorithm)
            mapOf(
                "access_token" to "access-token",
                "refresh_token" to "refresh-token",
                "id_token" to idToken,
            )
        } ?: throw IllegalStateException("Private key not found")
    }

    @GetMapping("/auth/userinfo")
    fun getUserInfo() = mapOf(
        "email" to "<EMAIL>",
        "name" to "<NAME>",
    )

    companion object {
        const val CODE: String = "code"
    }

}
