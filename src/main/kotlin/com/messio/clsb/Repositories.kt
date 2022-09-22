package com.messio.clsb

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository


@Component
class Facade(
    val bankRepository: BankRepository,
    val currencyRepository: CurrencyRepository,
    val accountRepository: AccountRepository,
)

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
    @Query("select a from Account a where a.bank = ?1 and a.denomination = ''")
    fun findMirror(bank: Bank): Account?
    fun findTopByBankAndDenomination(bank: Bank, denomination: String): Account?
    fun findByBank(bank: Bank): Iterable<Account>
}

