package com.messio.clsb

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
    fun findTopByIso(iso: String): Currency?
}



@Repository
interface AccountRepository: CrudRepository<Account, Long> {
    fun findTopByDenomination(denomination: String): Account?
    fun findByBank(bank: Bank): Iterable<Account>
}

