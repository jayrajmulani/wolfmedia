DELETE FROM RL_PAY WHERE 1=1;
ALTER TABLE RL_PAY AUTO_INCREMENT = 1;
insert into RL_PAY (record_label_id, service_id, amount, timestamp) values (3001, 1, 3.3, '2023-01-01 12:00:00');
insert into RL_PAY (record_label_id, service_id, amount, timestamp) values (3001, 1, 6.6, '2023-02-01 12:00:00');
insert into RL_PAY (record_label_id, service_id, amount, timestamp) values (3001, 1, 9.9, '2023-03-01 12:00:00');
insert into RL_PAY (record_label_id, service_id, amount, timestamp) values (3002, 1, 330, '2023-01-01 12:00:00');
insert into RL_PAY (record_label_id, service_id, amount, timestamp) values (3002, 1, 660, '2023-02-01 12:00:00');
insert into RL_PAY (record_label_id, service_id, amount, timestamp) values (3002, 1, 990, '2023-03-01 12:00:00');
