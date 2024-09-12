package com.messio.clsb.controllers

import com.messio.clsb.Facade
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ApiController(
    facade: Facade
) {
    @GetMapping
    fun apiIndex() = mapOf("status" to "ok")
}
