package com.messio.clsb

import legacyb1.Movements
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
    val transferRepository: TransferRepository,
    val payInRepository: PayInRepository,
    val payOutRepository: PayOutRepository,
    val tradeRepository: TradeRepository,
    val settlementRepository: SettlementRepository,
    val movementRepository: MovementRepository,
) {
    fun book(instructions: Collection<Instruction>, moment: LocalDateTime): Balance {
        val balance = Balance()
        val movements = instructions.map { Movement(instruction = it, db = it.principal, cr = it.counterparty, amount = it.amount) }
        instructions.forEach { it.executed = moment }
        movementRepository.saveAll(movements)
        instructionRepository.saveAll(instructions)
        return balance
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
interface InstructionRepository: CrudRepository<Instruction, Long>

@Repository
interface TransferRepository: CrudRepository<Transfer, Long> {
    @Query("select t from Transfer t where t.executed is null and (t.execution is null or t.execution <= ?1)")
    fun findPendingTransfers(moment: LocalDateTime): Iterable<Transfer>
}

@Repository
interface PayInRepository: CrudRepository<PayIn, Long> {
    fun findByExecutedIsNull(): Iterable<PayIn>
}

@Repository
interface PayOutRepository: CrudRepository<PayOut, Long> {
    fun findByExecutedIsNull(): Iterable<PayOut>
}

@Repository
interface TradeRepository: CrudRepository<Trade, Long> {
    @Query("select t1, t2 from Trade t1, Trade t2 where t1.reference=t2.reference and t1.id<t2.id and t1.settlement=?1 and t2.settlement=?1 and t1.principal=t2.counterparty and t1.counterparty=t2.principal and t1.match is null and t2.match is null")
    fun findUnmatchedBySettlement(day: LocalDate): Iterable<Array<Any>>
    @Query("select max(t.match) from Trade t")
    fun findMaxMatch(): Long?
}

@Repository
interface SettlementRepository: CrudRepository<Settlement, Long>

@Repository
interface MovementRepository: CrudRepository<Movement, Long>
