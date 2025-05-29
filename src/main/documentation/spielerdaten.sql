create table spielerdaten
(
    uuid           varchar(36)   not null
        primary key,
    name           varchar(255) null,
    ip             varchar(255)  not null,
    verified       tinyint(1) default 0 null,
    discord_userid varchar(255)  not null,
    verified_date  varchar(255)  not null,
    spielzeit      int default 0 not null,
    coins          int           not null,
    insert_date  datetime     not null,
    last_updated datetime     not null
);