package com.messio.clsb

import org.springframework.context.ApplicationListener
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

    open fun opening(moment: LocalTime) {}
    open fun closing(moment: LocalTime) {}
    open fun bankOpening(moment: LocalTime, bank: Bank) {}
    open fun bankSettlementCompletionTarget(moment: LocalTime, bank: Bank) {}
    open fun bankClosing(moment: LocalTime, bank: Bank) {}
    open fun currencyOpening(moment: LocalTime, currency: Currency) {}
    open fun currencyFundingCompletionTarget(moment: LocalTime, currency: Currency) {}
    open fun currencyClose(moment: LocalTime, currency: Currency) {}
    open fun currencyClosing(moment: LocalTime, currency: Currency) {}
}

