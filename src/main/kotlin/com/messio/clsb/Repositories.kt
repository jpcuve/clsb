package com.messio.clsb

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime


@Component
class Facade(
    val bankRepository: BankRepository,
    val currencyRepository: CurrencyRepository,
    val accountRepository: AccountRepository,
    val instructionRepository: InstructionRepository,
    val tradeRepository: TradeRepository,
) {
    fun book(instruction: Instruction, moment: LocalDateTime) {
        val maxBookId = instructionRepository.findMaxBookId() ?: 0L
        instructionRepository.findById(instruction.id).ifPresent {
            logger.debug("Booking @ {}: {}", moment, instruction)
            it.bookId = maxBookId + 1
            it.booked = moment
            instructionRepository.save(it)
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(Facade::class.java)
    }
}

@Repository
interface BankRepository: CrudRepository<Bank, Long> {
    fun findTopByDenomination(denomination: String): Bank?
}

@Repository
interface CurrencyRepository: CrudRepository<Currency, Long> {
    fun findTopByBankAndIso(bank: Bank, iso: String): Currency?
    fun findByBank(bank: Bank): Iterable<Currency>
}



@Repository
interface AccountRepository: CrudRepository<Account, Long> {
    fun findTopByBankAndDenomination(bank: Bank, denomination: String): Account?
    fun findByBank(bank: Bank): Iterable<Account>
}

@Repository
interface InstructionRepository: CrudRepository<Instruction, Long> {
    @Query("select max(i.bookId) from Instruction i")
    fun findMaxBookId(): Long?
}

@Repository
interface TradeRepository: CrudRepository<Trade, Long> {
    @Query("select t1.ref, t1.id, t2.id, t1.amount from trades t1 join trades t2 on t1.ref=t2.ref and t1.day_settlement=?1 and t2.day_settlement=?1 and t1.party=t2.counterparty and t1.counterparty=t2.party", nativeQuery = true)
    fun findMatchesByDay(day: LocalDate): Iterable<Array<Any>>
}

