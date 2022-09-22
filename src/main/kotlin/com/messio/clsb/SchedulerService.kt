package com.messio.clsb

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class SchedulerService(
    val facade: Facade,
    val publisher: ApplicationEventPublisher,
) {
    fun events(): List<BaseEvent> {
        val events = mutableListOf<BaseEvent>()
        events.addAll(
            listOf(
                BaseEvent(LocalTime.MIN, "init"),
                BaseEvent(LocalTime.MAX, "done"),
            )
        )
        facade.bankRepository.findAll().forEach { bank ->
            events.addAll(
                listOf(
                    BankEvent(bank, bank.opening, "${bank.denomination} opening"),
                    BankEvent(bank, bank.settlementCompletionTarget, "${bank.denomination} sct"),
                    BankEvent(bank, bank.closing, "${bank.denomination} closing"),
                )
            )
            facade.currencyRepository.findByBank(bank).forEach { currency ->
                events.addAll(
                    listOf(
                        CurrencyEvent(currency, currency.opening, "${bank.denomination} ${currency.iso} opening"),
                        CurrencyEvent(currency, currency.fundingCompletionTarget, "${bank.denomination} ${currency.iso} fct"),
                        CurrencyEvent(currency, currency.closing, "${bank.denomination} ${currency.iso} closing"),
                        CurrencyEvent(currency, currency.close, "${bank.denomination} ${currency.iso} close"),
                    )
                )
            }
        }
        events.sortBy { it.moment }
        return events
    }
}