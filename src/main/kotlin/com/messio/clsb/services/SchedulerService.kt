package com.messio.clsb.services

import com.messio.clsb.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class SchedulerService(
    val facade: Facade,
    val publisher: ApplicationEventPublisher,
    @Value("\${app.start-date}") startDateAsString: String,
    @Value("\${app.simulation-duration-in-days}") simulationDuration: Long,
) {
    private final val events = mutableListOf<BaseEvent>()
    private final val startDate = LocalDate.parse(startDateAsString)

    init {
        (0 until simulationDuration).forEach {
            val day = startDate.plusDays(it)
            events.addAll(
                listOf(
                    BaseEvent(LocalDateTime.of(day, LocalTime.MIN), EventNature.OPENING),
                    BaseEvent(LocalDateTime.of(day, LocalTime.MAX), EventNature.CLOSING),
                )
            )
            facade.bankRepository.findAll().forEach { bank ->
                events.addAll(
                    listOf(
                        BankEvent(bank, LocalDateTime.of(day, bank.opening), EventNature.OPENING),
                        BankEvent(bank, LocalDateTime.of(day, bank.settlementCompletionTarget), EventNature.SCT),
                        BankEvent(bank, LocalDateTime.of(day, bank.closing), EventNature.CLOSING),
                    )
                )
                facade.currencyRepository.findByBank(bank).forEach { currency ->
                    events.addAll(
                        listOf(
                            CurrencyEvent(currency, LocalDateTime.of(day, currency.opening), EventNature.OPENING),
                            CurrencyEvent(currency, LocalDateTime.of(day, currency.fundingCompletionTarget), EventNature.FCT),
                            CurrencyEvent(currency, LocalDateTime.of(day, currency.close), EventNature.CLOSE),
                            CurrencyEvent(currency, LocalDateTime.of(day, currency.closing), EventNature.CLOSING),
                        )
                    )
                }
            }
        }
        events.sortBy { it.moment }
        events.forEach { publisher.publishEvent(it) }
    }
}