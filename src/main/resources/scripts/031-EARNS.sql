DELETE FROM EARNS WHERE 1=1;
ALTER TABLE EARNS AUTO_INCREMENT = 1;
insert into EARNS (service_id, user_id, amount, timestamp) values (1, 8001, 555.5, '2023-01-01 12:00:00');
insert into EARNS (service_id, user_id, amount, timestamp) values (1, 8002, 555.5, '2023-01-01 12:00:00');

insert into EARNS (service_id, user_id, amount, timestamp) values (1, 8001, 1111, '2023-02-01 12:00:00');
insert into EARNS (service_id, user_id, amount, timestamp) values (1, 8002, 1111, '2023-02-01 12:00:00');

insert into EARNS (service_id, user_id, amount, timestamp) values (1, 8001, 1666.5, '2023-03-01 12:00:00');
insert into EARNS (service_id, user_id, amount, timestamp) values (1, 8002, 1666.5, '2023-03-01 12:00:00');

insert into EARNS (service_id, user_id, amount, timestamp) values (1, 8001, 61500, '2023-04-01 12:00:00');
insert into EARNS (service_id, user_id, amount, timestamp) values (1, 8002, 61500, '2023-04-01 12:00:00');
