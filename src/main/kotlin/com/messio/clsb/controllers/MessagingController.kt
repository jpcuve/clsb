package com.messio.clsb.controllers

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
class MessagingController {
    @MessageMapping("/echo")
    fun echo(@RequestBody message: String): String {
        println("Echo: $message")
        return message
    }
}
