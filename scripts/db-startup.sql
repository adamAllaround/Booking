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
    startTime TIMESTAMP NOT NULL,
    policy varchar(100) NOT NULL,
    parameters varchar(20) NOT NULL,
    UNIQUE(roomId, startTime)
);

CREATE TABLE IF NOT EXISTS reservations
(
    reservationId UUID NOT NULL,
    roomId UUID NOT NULL,
    dateFrom DATE NOT NULL,
    dateTo DATE NOT NULL,
    UNIQUE(roomId, reservationId)
);