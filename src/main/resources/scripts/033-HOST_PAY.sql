DELETE FROM HOST_PAY WHERE 1=1;
ALTER TABLE HOST_PAY AUTO_INCREMENT = 1;

insert into HOST_PAY (host_id, service_id, amount, timestamp) values (6001, 1, 20, '2023-01-01 01:09:34');
insert into HOST_PAY (host_id, service_id, amount, timestamp) values (6001, 1, 30, '2023-02-01 01:09:34');
insert into HOST_PAY (host_id, service_id, amount, timestamp) values (6001, 1, 40, '2023-03-01 01:09:34');