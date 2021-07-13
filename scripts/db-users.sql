-- to be executed by admin user

CREATE DATABASE bookingdb
WITH
    ENCODING = 'UTF8'
    LC_COLLATE = 'Polish_Poland.1250'
    LC_CTYPE = 'Polish_Poland.1250'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- connect to bookingdb as admin
CREATE SCHEMA bookingschema;

REVOKE CREATE ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON DATABASE bookingdb FROM PUBLIC;
CREATE ROLE readonly;
GRANT CONNECT ON DATABASE bookingdb TO readonly;

CREATE ROLE readwrite;
GRANT CONNECT ON DATABASE bookingdb TO readwrite;

CREATE ROLE alterschema;
GRANT CONNECT ON DATABASE bookingdb TO alterschema;
GRANT USAGE ON SCHEMA bookingschema TO alterschema;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA bookingschema TO alterschema WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON SCHEMA bookingschema TO alterschema WITH GRANT OPTION;
ALTER DEFAULT PRIVILEGES IN SCHEMA bookingschema GRANT ALL PRIVILEGES ON TABLES TO alterschema WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA bookingschema TO alterschema WITH GRANT OPTION;
ALTER DEFAULT PRIVILEGES IN SCHEMA bookingschema GRANT ALL PRIVILEGES ON SEQUENCES TO alterschema WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA bookingschema TO alterschema WITH GRANT OPTION;

CREATE USER bookingapp;
GRANT readwrite to bookingapp;

CREATE USER bookingadmin;
GRANT alterschema to bookingadmin;

ALTER ROLE bookingapp SET search_path =bookingschema;
ALTER ROLE bookingadmin SET search_path =bookingschema;

--connect as bookingadmin
GRANT USAGE ON SCHEMA bookingschema TO readonly;
GRANT SELECT ON ALL TABLES IN SCHEMA bookingschema TO readonly;
ALTER DEFAULT PRIVILEGES IN SCHEMA bookingschema GRANT SELECT ON TABLES TO readonly;  --makes sure readyonly is able to select newly created tables

GRANT USAGE ON SCHEMA bookingschema TO readwrite;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA bookingschema TO readwrite;
ALTER DEFAULT PRIVILEGES IN SCHEMA bookingschema GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO readwrite;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA bookingschema TO readwrite;
ALTER DEFAULT PRIVILEGES IN SCHEMA bookingschema GRANT USAGE ON SEQUENCES TO readwrite;

--ALTER DEFAULT PRIVILEGES FOR ROLE alterschema GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO readwrite;
--ALTER DEFAULT PRIVILEGES FOR ROLE alterschema GRANT SELECT ON TABLES  TO readwrite;





