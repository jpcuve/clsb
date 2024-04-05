create table accounts
(
    id           bigint       not null auto_increment,
    denomination varchar(255) not null,
    collateral   varchar(255) not null,
    eligible     boolean      not null,
    short_limit  varchar(255) not null,
    suspended    boolean      not null,
    bank_id      bigint,
    primary key (id)
);
create table banks
(
    id                                bigint       not null auto_increment,
    denomination                      varchar(255) not null,
    when_opening                      varchar(8)   not null,
    when_settlement_completion_target varchar(8)   not null,
    when_closing                      varchar(8)   not null,
    minimum_pay_in                    varchar(255) not null,
    base_iso                          varchar(3)   not null,
    overall_short_limit               varchar(255) not null,
    primary key (id)
);
create table currencies
(
    id                             bigint         not null auto_increment,
    iso                            varchar(255)   not null,
    currency_group                 varchar(191)   not null,
    when_opening                   varchar(8)     not null,
    when_funding_completion_target varchar(8)     not null,
    when_close                     varchar(8)     not null,
    when_closing                   varchar(8)     not null,
    volatility_margin              decimal(6, 3)  not null,
    base_rate                      decimal(10, 5) not null,
    scale                          int            not null,
    pay_in_schedule                varchar(255)   not null,
    bank_id                        bigint         not null,
    primary key (id)
);
create table currency_rtgs_periods
(
    currency_id bigint     not null,
    when_init   varchar(8) not null,
    when_done   varchar(8) not null
);
create table instructions
(
    id               bigint       not null auto_increment,
    when_execution   varchar(26)  not null,
    when_booked      varchar(26),
    book_id          bigint,
    cr_id            bigint       not null,
    db_id            bigint       not null,
    instruction_type varchar(64)  not null,
    reference        varchar(255) not null,
    amount           varchar(255) not null,
    primary key (id)
);
create table trades
(
    id              bigint       not null auto_increment,
    day_settlement  varchar(10)  not null,
    principal_id    bigint       not null,
    counterparty_id bigint       not null,
    ref             varchar(255) not null,
    amount          varchar(255) not null,
    settled         boolean      not null,
    primary key (id)
);
alter table accounts
    add constraint uk_account_bank_denomination unique (bank_id, denomination);
alter table banks
    add constraint uk_account_denomination unique (denomination);
alter table currencies
    add constraint uk_currency_bank_iso unique (bank_id, iso);
alter table accounts
    add constraint fk_account_bank foreign key (bank_id) references banks (id);
alter table currencies
    add constraint fk_currency_bank foreign key (bank_id) references banks (id);
alter table instructions
    add constraint fk_instruction_account_db foreign key (db_id) references accounts (id);
alter table instructions
    add constraint fk_instruction_account_cr foreign key (cr_id) references accounts (id);
alter table currency_rtgs_periods
    add constraint fk_rtgs_period_currency foreign key (currency_id) references currencies (id);
alter table trades
    add constraint fk_trade_account_principal foreign key (principal_id) references accounts (id);
alter table trades
    add constraint fk_trade_account_counterparty foreign key (counterparty_id) references accounts (id);
