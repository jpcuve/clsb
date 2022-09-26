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
    bank_id                        bigint,
    primary key (id)
);
alter table accounts
    add unique (denomination);
alter table banks
    add unique (denomination);
alter table currencies
    add unique (iso);
alter table accounts
    add foreign key (bank_id) references banks (id);
alter table currencies
    add foreign key (bank_id) references banks (id);