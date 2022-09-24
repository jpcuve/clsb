package com.messio.clsb

import org.springframework.context.ApplicationEvent
import java.time.LocalTime

enum class EventNature {
    OPENING, CLOSING,
    SCT, FCT, CLOSE
}

open class BaseEvent(val moment: LocalTime, val nature: EventNature): ApplicationEvent("clsb")

class BankEvent(val bank: Bank, moment: LocalTime, nature: EventNature): BaseEvent(moment, nature)

class CurrencyEvent(val currency: Currency, moment: LocalTime, nature: EventNature): BaseEvent(moment, nature)