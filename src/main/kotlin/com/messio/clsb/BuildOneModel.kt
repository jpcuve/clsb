package com.messio.clsb

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.atomic.AtomicInteger

@Component
class BuildOneModel(
    val facade: Facade,
    @Value("\${app.mirror-name}") val mirrorName: String,
): BankModel() {
    override fun currencyClosing(moment: LocalDateTime, currency: Currency) {
        logger.debug("Closing currency: ${currency.iso}")
        facade.instructionRepository.findAll()
            .filter { !it.execution.isAfter(moment) && it.type == InstructionType.PAY_OUT && it.amount.containsKey(currency.iso) && it.booked == null}
            .forEach { facade.book(it, moment) }
    }

    override fun bankSettlementCompletionTarget(moment: LocalDateTime, bank: Bank) {
        logger.debug("Booking pay-ins")
        val balance = Balance()
        facade.instructionRepository.findAll()
            .filter { !it.execution.isAfter(moment) && it.type == InstructionType.PAY_IN && it.booked == null }
            .forEach {
                logger.debug("Booking: {}", it)
                facade.book(it, moment)
                balance.transfer(it.db, it.cr, it.amount)
            }
        logger.debug("Settling sequentially")
        val settledCount = AtomicInteger()
        do {
            settledCount.set(0)
            // simplest stuff, run once and only allow if sufficient provision on account
            facade.instructionRepository.findAll()
                .filter { !it.execution.isAfter(moment) && it.type == InstructionType.SETTLEMENT && it.booked == null && balance.isProvisioned(it.db, it.amount) }
                .forEach {
                    facade.book(it, moment)
                    balance.transfer(it.db, it.cr, it.amount)
                    settledCount.incrementAndGet()
                }
            logger.debug("Count of instructions settled: {}", settledCount)
        } while (settledCount.get() > 0)
        logger.debug("Generating pay-outs")
        facade.accountRepository.findTopByBankAndDenomination(bank, mirrorName)?.let { mirror ->
            balance
                .filter { it.key != mirrorName }
                .forEach { e ->
                    facade.accountRepository.findTopByBankAndDenomination(bank, e.key)?.let { db ->
                        e.value.xlong().forEach {
                            val payout = Instruction(
                                db = db,
                                cr = mirror,
                                execution = moment,
                                type = InstructionType.PAY_OUT,
                                amount = Position(it),
                                reference = "Pay-out ${it.key} to ${e.key}"
                            )
                            logger.debug("Creating pay-out: {}", payout)
                            facade.instructionRepository.save(payout)
                        }
                    }
                }
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(BuildOneModel::class.java)
    }
}