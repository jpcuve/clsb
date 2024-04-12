package com.messio.clsb

import com.messio.clsb.services.MatchingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BuildOneModel(
    val facade: Facade,
    val matchingService: MatchingService,
): BankModel() {
    override fun currencyClosing(moment: LocalDateTime, currency: Currency) {
        logger.debug("Closing currency: ${currency.iso}")
        val payOuts = facade.payOutRepository.findByExecutedIsNull()
            .filter { it.amount.containsKey(currency.iso)}
            .toList()
        facade.book(payOuts, moment)
    }

    override fun bankSettlementCompletionTarget(moment: LocalDateTime, bank: Bank) {
        logger.debug("Booking pay-ins")
        val balance = Balance()
        val instructions = mutableListOf<Instruction>()
        instructions.addAll(facade.transferRepository.findPendingTransfers(moment).toList())
        instructions.addAll(facade.payInRepository.findByExecutedIsNull().toList())
        facade.book(instructions, moment)
        logger.debug("Assembling settlement queue")
        matchingService.matchTrades(moment)
        logger.debug("Settling sequentially")
/*
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
*/
        logger.debug("Generating pay-outs")
        bank.mirror?.let { mirror ->
            balance
                .filter { it.key != mirror }
                .forEach {
                    it.value.xlong().forEach { e ->
                        val payOut = PayOut(
                            bank = bank,
                            counterparty = it.key,
                            amount = Position(e),
                            reference = "Pay-out ${it.key} to ${e.key}"
                        )
                        logger.debug("Creating pay-out: {}", payOut)
                        facade.payOutRepository.save(payOut)
                    }
                }
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(BuildOneModel::class.java)
    }
}
