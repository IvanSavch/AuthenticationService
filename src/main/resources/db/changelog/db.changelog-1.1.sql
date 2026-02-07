-- liquibase formatted sql

--changeset Sauchanka:2
alter table refresh_token
    alter column expiration_date type timestamp using expiration_date::timestamp;
