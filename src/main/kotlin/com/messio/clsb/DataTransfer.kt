package com.messio.clsb

data class PerpetualValue (
    val accounts: List<Account> = emptyList(),
    val currencies: List<Currency> = emptyList(),
)

