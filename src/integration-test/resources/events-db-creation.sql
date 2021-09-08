CREATE TABLE IF NOT EXISTS Events
(
    id        uuid PRIMARY KEY,
    type      varchar(100) NOT NULL,
    created   timestamp    NOT NULL,
    published boolean      NOT NULL,
    subjectId uuid         NOT NULL,
    payload   json         not null
);

CREATE TABLE IF NOT EXISTS Owners
(
    id    uuid PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS ItemsOwners
(
    id      uuid PRIMARY KEY,
    created timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS Items
(
    id             uuid PRIMARY KEY,
    ownerId        uuid         NOT NULL,
    name           VARCHAR(100) NOT NULL,
    capacity       INT          NOT NULL,
    location       VARCHAR(100) NOT NULL,
    type           VARCHAR(100) NOT NULL,
    hotelHourStart TIME         NULL,
    hotelHourEnd   TIME         NULL
);

CREATE TABLE IF NOT EXISTS OccupationItems
(
    id             uuid PRIMARY KEY,
    created        timestamp    NOT NULL,
    type           VARCHAR(100) NOT NULL,
    hotelHourStart TIME         NULL,
    hotelHourEnd   TIME         NULL
);

CREATE TABLE IF NOT EXISTS Availabilities
(
    id        uuid PRIMARY KEY,
    itemId    uuid      NOT NULL,
    startTime timestamp NOT NULL,
    endTime   timestamp NOT NULL,
    bookingId uuid      NULL
);

CREATE TABLE IF NOT EXISTS Bookings
(
    id        uuid PRIMARY KEY,
    itemId    uuid      NOT NULL,
    startTime timestamp NOT NULL,
    endTime   timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS NotificationEvents
(
    id      uuid PRIMARY KEY,
    created timestamp    NOT NULL,
    sent    boolean      NOT NULL,
    type    VARCHAR(100) NOT NULL,
    payload json         not null
);

CREATE TABLE IF NOT EXISTS NotificationsOwners
(
    id    uuid PRIMARY KEY,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS NotificationsItems
(
    id             uuid PRIMARY KEY,
    ownerId        uuid NOT NULL,
    hotelHourStart TIME NULL,
    hotelHourEnd   TIME NULL
);

CREATE TABLE IF NOT EXISTS Messages
(
    id        uuid PRIMARY KEY,
    eventId   uuid         NOT NULL,
    sent      BOOLEAN      NOT NULL,
    recipient VARCHAR(100) NOT NULL,
    content   TEXT         NOT NULL
);

