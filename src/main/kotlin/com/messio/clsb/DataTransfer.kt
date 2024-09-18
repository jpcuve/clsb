package com.messio.clsb

data class PerpetualValue (
    val bank: Bank,
    val accounts: List<Account> = emptyList(),
    val currencies: List<Currency> = emptyList(),
)

