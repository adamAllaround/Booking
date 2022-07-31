CREATE TABLE IF NOT EXISTS owners
(
    id      uuid PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    email   VARCHAR(100) NOT NULL,
    created timestamp    NOT NULL
);

CREATE TABLE IF NOT EXISTS roommeta
(
    id            uuid PRIMARY KEY,
    ownerId       uuid         NOT NULL,
    name          VARCHAR(100) NOT NULL,
    description   TEXT         NOT NULL,
    capacity      INT          NOT NULL,
    location      VARCHAR(100) NOT NULL,
    arrivalHour   TIME         NULL,
    departureHour TIME         NULL
);

CREATE TABLE IF NOT EXISTS pricingpolicies
(
    roomId  UUID NOT NULL,
    startTime TIMESTAMP NOT NULL UNIQUE,
    policy varchar(100) NOT NULL,
    parameters varchar(20) NOT NULL
);

insert into pricingpolicies (roomId, startTime, policy, parameters) values (gen_random_uuid(), '2022-08-03'::timestamp, 'FIXED', '213')


select * from pricingpolicies where startTime =
                                    (select max(startTime) from pricingpolicies where startTime <='2022-07-31'::TIMESTAMP)
UNION SELECT * FROM PRICINGPOLICIES WHERE STARTTIME = (SELECT MAX(STARTTIME) FROM PRICINGPOLICIES WHERE STARTTIME < '2022-08-01');