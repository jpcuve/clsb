package com.messio.clsb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalTime
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime


@Converter
class PositionConverter: AttributeConverter<Position, String>{
    override fun convertToDatabaseColumn(attribute: Position?): String? = attribute?.toString()
    override fun convertToEntityAttribute(dbData: String?): Position? = dbData?.let { Position.parse(dbData) }
}

@Converter
class ScheduleConverter: AttributeConverter<Schedule, String>{
    override fun convertToDatabaseColumn(attribute: Schedule?): String? = attribute?.toString()
    override fun convertToEntityAttribute(dbData: String?): Schedule? = dbData?.let { Schedule.parse(dbData) }
}

@Entity
@Table(name = "banks")
class Bank(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") var id: Long = 0L,
    @Column(name = "denomination", nullable = false, unique = true) var denomination: String = "",
    @Column(name = "when_opening", nullable = false) var opening: LocalTime = LocalTime.of(8, 0),
    @Column(name = "when_closing", nullable = false) var closing: LocalTime = LocalTime.of(17, 0),
    @Column(name = "when_settlement_completion_target", nullable = false) var settlementCompletionTarget: LocalTime = LocalTime.of(10, 0),
    @Convert(converter = PositionConverter::class) @Column(name = "minimum_pay_in", nullable = false) var minimumPayIn: Position = Position.ZERO,
    @Column(name = "base_iso", nullable = false) var baseIso: String = "",
    @Convert(converter = PositionConverter::class) @Column(name = "overall_short_limit", nullable = false) var overallShortLimit: Position = Position.ZERO,
    @OneToOne @JoinColumn(name = "mirror_id") var mirror: Account? = null,
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
    @ManyToOne @JoinColumn(name = "bank_id", nullable = false) var bank: Bank,
    @Column(name = "bank_id", insertable = false, updatable = false, nullable = false) var bankId: Long = 0L,
    @Column(name = "iso", nullable = false) var iso: String = "",
    @Column(name = "when_opening", nullable = false) var opening: LocalTime = LocalTime.of(1, 0),
    @Column(name = "when_closing", nullable = false) var closing: LocalTime = LocalTime.of(23, 0),
    @Column(name = "when_funding_completion_target", nullable = false) var fundingCompletionTarget: LocalTime = LocalTime.of(6, 0),
    @Column(name = "when_close", nullable = false) var close: LocalTime = LocalTime.of(23, 59),
    @Column(name = "volatility_margin", nullable = false) var volatilityMargin: BigDecimal = BigDecimal.ZERO,
    @Column(name = "base_rate", nullable = false) var baseRate: BigDecimal = BigDecimal.ZERO,
    @Column(name = "scale", nullable = false) var scale: Int = 0,
    @Convert(converter = ScheduleConverter::class) @Column(name = "pay_in_schedule") var payInSchedule: Schedule = Schedule.EMPTY,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "currency_rtgs_periods", joinColumns = [JoinColumn(name = "currency_id", nullable = false)])
    var realTimeGrossSettlementPeriods: MutableSet<RealTimeGrossSettlementPeriod> = mutableSetOf(),
)

@Embeddable
class RealTimeGrossSettlementPeriod(
    @Column(name = "when_init") var init: LocalTime = LocalTime.MIN,
    @Column(name = "when_done") var done: LocalTime = LocalTime.MAX,
)

@Entity
@Table(name = "accounts", uniqueConstraints = [UniqueConstraint(columnNames = ["bank_id", "denomination"])])
@JsonIgnoreProperties("bank")
class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") var id: Long = 0L,
    @Column(name = "bank_id", insertable = false, updatable = false, nullable = false) var bankId: Long = 0L,
    @ManyToOne @JoinColumn(name = "bank_id") var bank: Bank,
    @Column(name = "denomination", nullable = false) var denomination: String = "",
    @OneToMany(mappedBy = "account") var currencies: Set<AccountCurrency> = emptySet(),
){
    override fun equals(other: Any?) = this === other || (other is Account && id == other.id)
    override fun hashCode() = id.hashCode()
}

@Entity
@Table(name = "account_currencies", uniqueConstraints = [UniqueConstraint(columnNames = ["account_id", "currency_id"])])
class AccountCurrency(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") var id: Long = 0L,
    @Column(name = "account_id", insertable = false, updatable = false, nullable = false) var accountId: Long = 0L,
    @ManyToOne @JoinColumn(name = "account_id") var account: Account,
    @Column(name = "currency_id", insertable = false, updatable = false, nullable = false) var currencyId: Long = 0L,
    @ManyToOne @JoinColumn(name = "currency_id") var currency: Currency,
    @Column(name = "suspended", nullable = false) var suspended: Boolean = true,
    @Column(name = "short_limit", nullable = false) var shortLimit: BigDecimal = BigDecimal.ZERO,
    @Column(name = "collateral", nullable = false) var collateral: BigDecimal = BigDecimal.ZERO,
)

enum class InstructionType {
    Transfer, PayIn, PayOut, Settlement, Trade
}

@Entity
@Table(name = "instructions")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="instruction_type")
open class Instruction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") open var id: Long = 0,
    @Enumerated(EnumType.STRING) @Column(name = "instruction_type", nullable = false, insertable = false, updatable = false) open var type: InstructionType,
    @Column(name = "reference", nullable = false) open var reference: String = "",
    @ManyToOne @JoinColumn(name = "principal_id", nullable = false) open var principal: Account,
    @Column(name = "principal_id", insertable = false, updatable = false) open var principalId: Long = 0L,
    @ManyToOne @JoinColumn(name = "counterparty_id", nullable = false) open var counterparty: Account,
    @Column(name = "counterparty_id", insertable = false, updatable = false) open var counterpartyId: Long = 0L,
    @Convert(converter = PositionConverter::class) @Column(name = "amount", nullable = false) open var amount: Position = Position.ZERO,
    @Column(name = "when_executed") open var executed: LocalDateTime? = null,
)

@Entity
@Table(name = "transfers")
class Transfer(
    principal: Account,
    counterparty: Account,
    reference: String,
    amount: Position,
    @Column(name = "when_execution") var execution: LocalDateTime? = null,
):Instruction(type = InstructionType.Transfer, principal = principal, counterparty = counterparty, reference = reference, amount = amount)

@Entity
@Table(name = "pay_ins")
class PayIn(
    principal: Account,
    bank: Bank,
    reference: String,
    amount: Position,
):Instruction(type = InstructionType.PayIn, principal = principal, counterparty = bank.mirror ?: throw RuntimeException(), reference = reference, amount = amount)

@Entity
@Table(name = "pay_outs")
class PayOut(
    bank: Bank,
    counterparty: Account,
    reference: String,
    amount: Position,
):Instruction(type = InstructionType.PayOut, principal = bank.mirror ?: throw RuntimeException(), counterparty = counterparty, reference = reference, amount = amount)

@Entity
@Table(name = "settlements")
class Settlement(
    principal: Account,
    counterparty: Account,
    amount: Position,
    match: Long,
): Instruction(type = InstructionType.Settlement, principal = principal, counterparty = counterparty, amount = amount, reference = "Match n° $match")

@Entity
@Table(name = "trades")
class Trade(
    principal: Account,
    counterparty: Account,
    reference: String,
    amount: Position,
    @Column(name = "when_settlement", nullable = false) var settlement: LocalDate = LocalDate.now(),
    @Column(name = "match") var match: Long? = null,
): Instruction(type = InstructionType.Trade, principal = principal, counterparty = counterparty, reference = reference, amount = amount)

@Entity
@Table(name = "movements")
class Movement(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") var id: Long = 0,
    @ManyToOne @JoinColumn(name = "instruction_id", nullable = false) var instruction: Instruction,
    @Column(name = "instruction_id", insertable = false, updatable = false) var instructionId: Long = 0L,
    @ManyToOne @JoinColumn(name = "db_id", nullable = false) var db: Account,
    @Column(name = "db_id", insertable = false, updatable = false) var dbId: Long = 0L,
    @ManyToOne @JoinColumn(name = "cr_id", nullable = false) var cr: Account,
    @Column(name = "cr_id", insertable = false, updatable = false) var crId: Long = 0L,
    @Convert(converter = PositionConverter::class) @Column(name = "amount", nullable = false) var amount: Position = Position.ZERO,
)

/*
@Entity
@Table(name = "instructions")
@JsonIgnoreProperties("db", "cr")
class Instruction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") var id: Long = 0,
    @Column(name = "when_execution", nullable = false) var execution: LocalDateTime = LocalDateTime.MIN,
    @Column(name = "book_id", nullable = true) var bookId: Long? = null,
    @Column(name = "when_booked", nullable = true) var booked: LocalDateTime? = null,
    @Enumerated(EnumType.STRING) @Column(name = "instruction_type", nullable = false) var type: InstructionType = InstructionType.PAY,
    @Column(name = "reference", nullable = false) var reference: String = "",
    @Convert(converter = PositionConverter::class) @Column(name = "amount", nullable = false) var amount: Position = Position.ZERO,
    @ManyToOne @JoinColumn(name = "db_id", nullable = false) var db: Account,
    @Column(name = "db_id", insertable = false, updatable = false) var dbId: Long = 0L,
    @ManyToOne @JoinColumn(name = "cr_id", nullable = false) var cr: Account,
    @Column(name = "cr_id", insertable = false, updatable = false) var crId: Long = 0L,
)

@Entity
@Table(name = "trades")
class Trade(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") var id: Long = 0,
    @Column(name = "day_settlement", nullable = false) var daySettlement: LocalDate = LocalDate.now(),
    @Column(name = "ref", nullable = false) var reference: String,
    @Convert(converter = PositionConverter::class) @Column(name = "amount", nullable = false) var amount: Position,
    @Column(name = "settled", nullable = false) var settled: Boolean = false,
    @ManyToOne @JoinColumn(name = "principal_id", nullable = false) var principal: Account,
    @Column(name = "principal_id", insertable = false, updatable = false) var principalId: Long = 0L,
    @ManyToOne @JoinColumn(name = "counterparty_id", nullable = false) var counterparty: Account,
    @Column(name = "counterparty_id", insertable = false, updatable = false) var counterpartyId: Long = 0L,
)
*/
