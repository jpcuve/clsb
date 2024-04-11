package com.messio.clsb

import com.messio.clsb.services.MatchingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BuildOneModel(
    val facade: Facade,
    val matchingService: MatchingService,
    @Value("\${app.mirror-name}") val mirrorName: String,
): BankModel() {
    override fun currencyClosing(moment: LocalDateTime, currency: Currency) {
        logger.debug("Closing currency: ${currency.iso}")
        val transfers = facade.transferRepository.findPendingTransfers(moment)
            .filter { it.type == InstructionType.PAY_OUT && it.amount.containsKey(currency.iso)}
            .toList()
        facade.book(transfers, moment)
    }

    override fun bankSettlementCompletionTarget(moment: LocalDateTime, bank: Bank) {
        logger.debug("Booking pay-ins")
        val balance = Balance()
        val transfers = facade.transferRepository.findPendingTransfers(moment)
            .filter { it.type == InstructionType.PAY_IN }
            .toList()
        facade.book(transfers, moment)
        logger.debug("Assembling settlement queue")
        matchingService.matchTrades(moment)
        logger.debug("Settling sequentially")
        var settledCount: Int
        do {
            settledCount = 0
            // simplest stuff, run once and only allow if sufficient provision on account
            facade.settlementRepository.findAll().forEach {
                if (balance.isProvisioned(it.first.principal, it.first.amount)){
                    facade.book(listOf(it), moment)
                    balance.transfer(instruction.db, instruction.cr, instruction.amount)
                    settledCount++
                }
            }
            logger.debug("Count of instructions settled: {}", settledCount)
        } while (settledCount > 0)
        logger.debug("Generating pay-outs")
        facade.accountRepository.findTopByBankAndDenomination(bank, mirrorName)?.let { mirror ->
            balance
                .filter { it.key.denomination != mirrorName }
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
