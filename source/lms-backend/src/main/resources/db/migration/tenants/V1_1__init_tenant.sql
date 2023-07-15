CREATE TABLE "tenant_config"
(
    "id"      varchar(50) PRIMARY KEY,
    "name"    varchar(200),
    "address" varchar(200),
    "avatar"  text
);


CREATE TYPE account_type as ENUM ('STAFF', 'STUDENT');
CREATE TABLE users
(
    "id"           serial PRIMARY KEY,
    "username"     varchar(50),
    "first_name"   varchar(50) not null,
    "last_name"    varchar(50),
    "email"        varchar(50) not null,
    "password"     text,
    "phone"        varchar(20),
    "avatar"       text,
    "account_type" account_type,
    "created_at"   timestamp default current_timestamp
);