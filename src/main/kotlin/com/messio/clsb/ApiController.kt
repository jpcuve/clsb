package com.messio.clsb

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/api")
class ApiController(
    facade: Facade
) {
    @GetMapping
    fun apiIndex() = mapOf("status" to "ok")
}