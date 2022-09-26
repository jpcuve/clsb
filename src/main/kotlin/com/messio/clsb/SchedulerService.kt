package com.messio.clsb

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class SchedulerService(
    val facade: Facade,
    val publisher: ApplicationEventPublisher,
) {
    private final val events = mutableListOf<BaseEvent>()
    init {
        events.addAll(
            listOf(
                BaseEvent(LocalTime.MIN, EventNature.OPENING),
                BaseEvent(LocalTime.MAX, EventNature.CLOSING),
            )
        )
        facade.bankRepository.findAll().forEach { bank ->
            events.addAll(
                listOf(
                    BankEvent(bank, bank.opening, EventNature.OPENING),
                    BankEvent(bank, bank.settlementCompletionTarget, EventNature.SCT),
                    BankEvent(bank, bank.closing, EventNature.CLOSING),
                )
            )
            facade.currencyRepository.findByBank(bank).forEach { currency ->
                events.addAll(
                    listOf(
                        CurrencyEvent(currency, currency.opening, EventNature.OPENING),
                        CurrencyEvent(currency, currency.fundingCompletionTarget, EventNature.FCT),
                        CurrencyEvent(currency, currency.close, EventNature.CLOSE),
                        CurrencyEvent(currency, currency.closing, EventNature.CLOSING),
                    )
                )
            }
        }
        events.sortBy { it.moment }
        events.forEach { publisher.publishEvent(it) }
    }
}