insert into banks(id, denomination, opening, closing, settlement_completion_target, minimum_pay_in)
values (1, 'TEST-01', '00:15', '23:00', '10:00', '{}');
insert into currencies(id, bank_id, iso, currency_group, opening, funding_completion_target, closing, close)
values (1, 1, 'EUR', 'EUROPE', '07:00', '09:00', '17:30', '18:00'),
       (2, 1, 'GBP', 'EUROPE', '06:00', '08:00', '14:30', '15:00'),
       (3, 1, 'USD', 'AMERICA', '06:30', '08:30', '21:00', '21:30'),
       (4, 1, 'JPY', 'ASIA', '03:00', '05:00', '13:30', '13:00');
insert into accounts(id, bank_id, denomination, eligible, suspended, short_limit, collateral)
values (1, 1, '', true, true, '{}', '{}'),
       (2, 1, 'a', true, true, '{}', '{}'),
       (3, 1, 'b', true, true, '{}', '{}'),
       (4, 1, 'c', true, true, '{}', '{}');
