package com.messio.clsb

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository


@Component
class Facade(
    val bankRepository: BankRepository,
    val accountRepository: AccountRepository,
)

@Repository
interface BankRepository: CrudRepository<Bank, Long> {
    fun findTopByDenomination(denomination: String): Bank?
}

@Repository
interface AccountRepository: CrudRepository<Account, Long> {
    fun findTopByDenomination(denomination: String): Account?
    fun findByBank(bank: Bank): Iterable<Account>
}

