package com.messio.clsb

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime

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
        var settledCount: Int
        do {
            settledCount = 0
            // simplest stuff, run once and only allow if sufficient provision on account
            facade.tradeRepository.findMatchesByDaySettlement(moment.toLocalDate(), false).forEach { match ->
                val trade = match[0] as Trade
                facade.accountRepository.findTopByBankAndDenomination(bank, trade.party)?.let { db ->
                    facade.accountRepository.findTopByBankAndDenomination(bank, trade.counterparty)?.let { cr ->
                        if (balance.isProvisioned(db, trade.amount)){
                            val instruction = facade.instructionRepository.save(
                                Instruction(
                                    execution = moment,
                                    type = InstructionType.SETTLEMENT,
                                    reference = trade.reference,
                                    amount = trade.amount,
                                    db = db,
                                    cr = cr,
                                )
                            )
                            facade.book(instruction, moment)
                            balance.transfer(instruction.db, instruction.cr, instruction.amount)
                            settledCount++
                            match.forEach { o ->
                                val t = o as Trade
                                t.settled = true
                                facade.tradeRepository.save(t)
                            }
                        }
                    }
                }
            }
/*
            facade.instructionRepository.findAll()
                .filter { !it.execution.isAfter(moment) && it.type == InstructionType.SETTLEMENT && it.booked == null && balance.isProvisioned(it.db, it.amount) }
                .forEach {
                    facade.book(it, moment)
                    balance.transfer(it.db, it.cr, it.amount)
                    settledCount++
                }
*/
            logger.debug("Count of instructions settled: {}", settledCount)
        } while (settledCount > 0)
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
