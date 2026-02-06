-- liquibase formatted sql

--changeset Sauchanka:1
create table users_credentials
(
    id         BIGSERIAL primary key not null,
    login      varchar(255) unique   not null,
    password   varchar(255)          not null,
    role       varchar(50)           not null,
    created_at timestamp,
    updated_at timestamp
);

create index users_credentials_login_idx
    on users_credentials (login);

create table refresh_token
(
    id              Bigserial primary key not null,
    user_id         BIGINT                not null,
    token           varchar(512) unique   not null,
    expiration_date date                  not null,
    created_at      timestamp,
    updated_at      timestamp,
    foreign key (user_id) references users_credentials(id) on DELETE cascade
);




