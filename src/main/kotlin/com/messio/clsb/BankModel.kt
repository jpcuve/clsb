package com.messio.clsb

import org.springframework.context.ApplicationListener
import java.time.LocalTime

abstract class BankModel: ApplicationListener<BaseEvent> {
    override fun onApplicationEvent(event: BaseEvent) {
        when (event){
            is CurrencyEvent -> when (event.nature){
                EventNature.OPENING -> currencyOpening(event.moment, event.currency)
                EventNature.FCT -> currencyFct(event.moment, event.currency)
                EventNature.CLOSE -> currencyClose(event.moment, event.currency)
                EventNature.CLOSING -> currencyClosing(event.moment, event.currency)
                else -> throw RuntimeException()
            }
            is BankEvent -> when (event.nature){
                EventNature.OPENING -> bankOpening(event.moment, event.bank)
                EventNature.SCT -> bankSct(event.moment, event.bank)
                EventNature.CLOSING -> bankClosing(event.moment, event.bank)
                else -> throw RuntimeException()
            }
            else -> when (event.nature){
                EventNature.OPENING -> opening(event.moment)
                EventNature.CLOSING -> closing(event.moment)
                else -> throw RuntimeException()
            }
        }
    }

    abstract fun opening(moment: LocalTime)
    abstract fun closing(moment: LocalTime)
    abstract fun bankOpening(moment: LocalTime, bank: Bank)
    abstract fun bankSct(moment: LocalTime, bank: Bank)
    abstract fun bankClosing(moment: LocalTime, bank: Bank)
    abstract fun currencyOpening(moment: LocalTime, currency: Currency)
    abstract fun currencyFct(moment: LocalTime, currency: Currency)
    abstract fun currencyClose(moment: LocalTime, currency: Currency)
    abstract fun currencyClosing(moment: LocalTime, currency: Currency)
}