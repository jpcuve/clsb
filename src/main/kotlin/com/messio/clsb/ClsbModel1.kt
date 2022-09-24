package com.messio.clsb

import org.springframework.stereotype.Component
import java.time.LocalTime

@Component
class ClsbModel1: BankModel() {
    override fun currencyOpening(moment: LocalTime, currency: Currency) {
        println("opening ${currency.iso}")
    }

    override fun currencyClosing(moment: LocalTime, currency: Currency) {
        println("closing ${currency.iso}")
    }
}