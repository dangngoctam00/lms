CREATE TABLE tenant
(
    tenant_id   varchar(50) primary key,
    schema      varchar(50) not null,
    username    varchar(50) not null,
    email       varchar(50) not null,
    phone       varchar(50) not null,
    password    text        not null,
    firstname   varchar(50) not null,
    lastname    varchar(50) not null,
    domain      varchar(50) not null,
    expire_time timestamp default current_timestamp
);