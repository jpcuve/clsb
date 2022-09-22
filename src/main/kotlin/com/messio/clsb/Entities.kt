package com.messio.clsb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalTime
import javax.persistence.*


@Converter
class PositionConverter: AttributeConverter<Position, String>{
    override fun convertToDatabaseColumn(attribute: Position?): String? = attribute?.toString()
    override fun convertToEntityAttribute(dbData: String?): Position? = dbData?.let { Position.parse(dbData) }
}


@Entity
@Table(name = "banks")
class Bank(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") var id: Long = 0L,
    @Column(name = "denomination", nullable = false, unique = true) var denomination: String = "",
    @Column(name = "opening", nullable = false) var opening: LocalTime = LocalTime.of(8, 0),
    @Column(name = "closing", nullable = false) var closing: LocalTime = LocalTime.of(17, 0),
    @Column(name = "settlement_completion_target", nullable = false) var settlementCompletionTarget: LocalTime = LocalTime.of(10, 0),
    @Convert(converter = PositionConverter::class) @Column(name = "minimum_pay_in", nullable = false) var minimumPayIn: Position = Position.ZERO,
)

enum class CurrencyGroup {
    ASIA, EUROPE, AMERICA
}

@Entity
@Table(name = "currencies", uniqueConstraints = [UniqueConstraint(columnNames = ["bank_id", "iso"])])
@JsonIgnoreProperties("bank")
class Currency(
    @Enumerated(EnumType.STRING) @Column(name = "currency_group", nullable = false) var currencyGroup: CurrencyGroup = CurrencyGroup.EUROPE,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") var id: Long = 0L,
    @Column(name = "bank_id", insertable = false, updatable = false, nullable = false) var bankId: Long = 0L,
    @Column(name = "iso", nullable = false, unique = true) var iso: String = "",
    @Column(name = "opening", nullable = false) var opening: LocalTime = LocalTime.of(1, 0),
    @Column(name = "closing", nullable = false) var closing: LocalTime = LocalTime.of(23, 0),
    @Column(name = "funding_completion_target", nullable = false) var fundingCompletionTarget: LocalTime = LocalTime.of(6, 0),
    @Column(name = "close", nullable = false) var close: LocalTime = LocalTime.of(23, 59),
){
    @ManyToOne @JoinColumn(name = "bank_id") lateinit var bank: Bank
}


@Entity
@Table(name = "accounts", uniqueConstraints = [UniqueConstraint(columnNames = ["bank_id", "denomination"])])
@JsonIgnoreProperties("bank")
class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") var id: Long = 0L,
    @Column(name = "bank_id", insertable = false, updatable = false, nullable = false) var bankId: Long = 0L,
    @Column(name = "denomination", nullable = false) var denomination: String = "",
    @Column(name = "eligible", nullable = false) var eligible: Boolean = true,
    @Column(name = "suspended", nullable = false) var suspended: Boolean = true,
    @Convert(converter = PositionConverter::class) @Column(name = "short_limit", nullable = false) var shortLimit: Position = Position.ZERO,
    @Convert(converter = PositionConverter::class) @Column(name = "collateral", nullable = false) var collateral: Position = Position.ZERO,
){
    @ManyToOne @JoinColumn(name = "bank_id") lateinit var bank: Bank
}
