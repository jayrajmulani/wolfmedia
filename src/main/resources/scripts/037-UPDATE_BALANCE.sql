update SERVICE set balance =
    (
        (select sum(amount) from EARNS where service_id = 1)
            - (select sum(amount) from HOST_PAY where service_id = 1)
            - (select sum(amount) from RL_PAY where service_id = 1)
    )
where id = 1;