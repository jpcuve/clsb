package com.messio.clsb

import org.springframework.context.ApplicationListener
import java.time.LocalDateTime
import java.time.LocalTime

open class BankModel: ApplicationListener<BaseEvent> {
    override fun onApplicationEvent(event: BaseEvent) {
        when (event) {
            is CurrencyEvent -> when (event.nature) {
                EventNature.OPENING -> currencyOpening(event.moment, event.currency)
                EventNature.FCT -> currencyFundingCompletionTarget(event.moment, event.currency)
                EventNature.CLOSE -> currencyClose(event.moment, event.currency)
                EventNature.CLOSING -> currencyClosing(event.moment, event.currency)
                else -> throw RuntimeException()
            }

            is BankEvent -> when (event.nature) {
                EventNature.OPENING -> bankOpening(event.moment, event.bank)
                EventNature.SCT -> bankSettlementCompletionTarget(event.moment, event.bank)
                EventNature.CLOSING -> bankClosing(event.moment, event.bank)
                else -> throw RuntimeException()
            }

            else -> when (event.nature) {
                EventNature.OPENING -> opening(event.moment)
                EventNature.CLOSING -> closing(event.moment)
                else -> throw RuntimeException()
            }
        }
    }

    open fun opening(moment: LocalDateTime) {}
    open fun closing(moment: LocalDateTime) {}
    open fun bankOpening(moment: LocalDateTime, bank: Bank) {}
    open fun bankSettlementCompletionTarget(moment: LocalDateTime, bank: Bank) {}
    open fun bankClosing(moment: LocalDateTime, bank: Bank) {}
    open fun currencyOpening(moment: LocalDateTime, currency: Currency) {}
    open fun currencyFundingCompletionTarget(moment: LocalDateTime, currency: Currency) {}
    open fun currencyClose(moment: LocalDateTime, currency: Currency) {}
    open fun currencyClosing(moment: LocalDateTime, currency: Currency) {}
}

