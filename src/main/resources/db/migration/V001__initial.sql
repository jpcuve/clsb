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
    when_opening                      time         not null,
    when_settlement_completion_target time         not null,
    when_closing                      time         not null,
    minimum_pay_in                    varchar(255) not null,
    primary key (id)
);
create table currencies
(
    id                             bigint       not null auto_increment,
    iso                            varchar(255) not null,
    currency_group                 varchar(191) not null,
    when_opening                   time         not null,
    when_funding_completion_target time         not null,
    when_close                     time         not null,
    when_closing                   time         not null,
    bank_id                        bigint       not null,
    primary key (id)
);
create table instructions
(
    id               bigint       not null auto_increment,
    when_execution   time         not null,
    when_booked      time,
    book_id          bigint,
    cr_id            bigint       not null,
    db_id            bigint       not null,
    instruction_type varchar(64)  not null,
    reference        varchar(255) not null,
    amount           varchar(255) not null,
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