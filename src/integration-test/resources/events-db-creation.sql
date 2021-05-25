CREATE TABLE IF NOT EXISTS Events
(
    id        VARCHAR(100) PRIMARY KEY,
    type      VARCHAR(100) NOT NULL,
    created   DATETIME     NOT NULL,
    published BOOLEAN      NOT NULL,
    subjectId VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Owners
(
    id      VARCHAR(100) PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    contact VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS ItemsOwners
(
    id      VARCHAR(100) PRIMARY KEY,
    created DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS Items
(
    id       VARCHAR(100) PRIMARY KEY,
    ownerId  VARCHAR(100) NOT NULL,
    name     VARCHAR(100) NOT NULL,
    capacity INT          NOT NULL,
    location VARCHAR(100) NOT NULL,
    type VARCHAR(100) NOT NULL,
    hotelHourStart TIME NULL,
    hotelHourEnd TIME NULL
);

CREATE TABLE IF NOT EXISTS OccupationItems
(
    id      VARCHAR(100) PRIMARY KEY,
    created DATETIME NOT NULL
);


CREATE TABLE IF NOT EXISTS Availabilities
(
    id      VARCHAR(100) PRIMARY KEY,
    itemId VARCHAR(100) NOT NULL,
    start   DATETIME     NOT NULL,
    end     DATETIME     NOT NULL
);

CREATE TABLE IF NOT EXISTS Bookings
(
    id      VARCHAR(100) PRIMARY KEY,
    itemId VARCHAR(100) NOT NULL,
    start   DATETIME     NOT NULL,
    end     DATETIME     NOT NULL
);

