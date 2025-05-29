create table forum_user
(
    email         varchar(255) null,
    username      varchar(255) null,
    password      varchar(255) null,
    rank          varchar(255) null,
    user_id       bigint not null
        primary key,
    creation_date datetime null,
    insert_date  datetime     not null,
    last_updated datetime     not null
);