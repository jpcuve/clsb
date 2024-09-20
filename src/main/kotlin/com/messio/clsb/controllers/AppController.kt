package com.messio.clsb.controllers

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class AppController: ErrorController {
    @RequestMapping("/")
    fun index() = "forward:index.html"

    @RequestMapping("/error")
    fun error() = "forward:/"
}
