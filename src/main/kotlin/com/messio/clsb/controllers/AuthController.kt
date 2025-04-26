package com.messio.clsb.controllers

import com.fasterxml.jackson.databind.JsonNode
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {
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

    @PostMapping("/auth/token")
    fun postToken(
        @RequestParam("grant_type") grantType: String,
        @RequestParam("code") code: String,
        @RequestParam("redirect_uri") redirectUri: String,
        @RequestParam("scope") scope: String,
    ): Map<String, String> {
        if (grantType != "authorization_code") throw IllegalArgumentException("Unsupported grant type: $grantType")
        if (code != CODE) throw IllegalArgumentException("Invalid code: $code")
        return mapOf(
            "access_token" to "access-token",
            "refresh_token" to "refresh-token",
            "id_token" to "id-token",
        )
    }

    companion object {
        const val CODE: String = "code"
    }

}