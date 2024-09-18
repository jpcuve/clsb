package com.messio.clsb.controllers

import com.messio.clsb.Facade
import com.messio.clsb.PerpetualValue
import jakarta.persistence.EntityNotFoundException
import jakarta.websocket.server.PathParam
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ApiController(
    val facade: Facade
) {
    @GetMapping
    fun getIndex() = mapOf("status" to "ok")

    @GetMapping("/banks")
    fun getBanks() = facade.bankRepository.findByOrderById()

    @GetMapping("/perpetual/{bank-id}")
    fun getPerpetual(@PathVariable("bank-id") bankId: Long) = facade.bankRepository.findByIdOrNull(bankId)?.let { bank ->
        PerpetualValue(
            bank = bank,
            accounts = facade.accountRepository.findByBank(bank).toList(),
            currencies = facade.currencyRepository.findByBank(bank).toList(),
        )
    } ?: throw EntityNotFoundException()
}
