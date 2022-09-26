create table accounts
(
    id           bigint       not null auto_increment,
    collateral   varchar(255) not null,
    denomination varchar(255) not null,
    eligible     boolean      not null,
    short_limit  varchar(255) not null,
    suspended    boolean      not null,
    bank_id      bigint,
    primary key (id)
);
create table banks
(
    id                           bigint       not null auto_increment,
    closing                      time         not null,
    denomination                 varchar(255) not null,
    minimum_pay_in               varchar(255) not null,
    opening                      time         not null,
    settlement_completion_target time         not null,
    primary key (id)
);
create table currencies
(
    id                        bigint       not null auto_increment,
    close                     time         not null,
    closing                   time         not null,
    currency_group            varchar(255) not null,
    funding_completion_target time         not null,
    iso                       varchar(255) not null,
    opening                   time         not null,
    bank_id                   bigint,
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