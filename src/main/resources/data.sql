insert into banks(id, denomination, opening, closing, settlement_completion_target, mirror, minimum_pay_in) values
    (1, 'TEST-01', '00:15', '23:00', '10:00', '{}', '{}');
insert into accounts(id, bank_id, denomination, eligible, suspended, short_limit, collateral) values
    (1, 1, 'a', true, true, '{}', '{}'),
    (2, 1, 'b', true, true, '{}', '{}'),
    (3, 1, 'c', true, true, '{}', '{}');
