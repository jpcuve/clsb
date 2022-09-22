package com.messio.clsb

import org.springframework.context.ApplicationEvent
import java.time.LocalTime

open class BaseEvent(val moment: LocalTime, val name: String): ApplicationEvent("clsb")

class BankEvent(val bank: Bank, moment: LocalTime, name: String): BaseEvent(moment, name)

class CurrencyEvent(val currency: Currency, moment: LocalTime, name: String): BaseEvent(moment, name)