package com.messio.clsb.controllers

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.exceptions.JWTVerificationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.RequestBody

@Controller
class MessagingController(
    val verifier: JWTVerifier,
) {
    fun verify(message: Message<*>) {
        (message.headers["nativeHeaders"] as LinkedMultiValueMap<*,*>)["id-token"]?.let {
            try {
                val decodedIdToken = verifier.verify(it.toString())
                val accountId = decodedIdToken.subject.toLong()
                val email = decodedIdToken.getClaim("email").asString()
                println(email)
            } catch (e: JWTVerificationException){
                logger.error(e.message, e)
            }
        }
    }

    @MessageMapping("/echo")
    @SendTo("/topic/ping")
    fun echo(@RequestBody message: Message<String>): String {
        println("Echo: $message")
//        verify(message)
        return message.payload
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
