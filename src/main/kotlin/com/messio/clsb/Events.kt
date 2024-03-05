package com.messio.clsb

import org.springframework.context.ApplicationEvent
import java.time.LocalDateTime
import java.time.LocalTime

enum class EventNature {
    OPENING, CLOSING,
    SCT, FCT, CLOSE
}

open class BaseEvent(val moment: LocalDateTime, val nature: EventNature): ApplicationEvent("clsb")

class BankEvent(val bank: Bank, moment: LocalDateTime, nature: EventNature): BaseEvent(moment, nature)

class CurrencyEvent(val currency: Currency, moment: LocalDateTime, nature: EventNature): BaseEvent(moment, nature)