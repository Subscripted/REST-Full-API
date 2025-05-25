create table forum_entbannungs_antraege_chat
(
    chat_id    bigint auto_increment
        primary key,
    sender_id  bigint null,
    message    varchar(255) null,
    message_id bigint null,
    time       datetime(6)  null,
    reported   tinyint(1)   null,
    antrags_id bigint null,
    constraint FKfwfa97nv9ia7ivwxn6vy5w9ow
        foreign key (sender_id) references forum_user (user_id),
    constraint FKkpuucxwr9vgyo0tuwp4qps5fe
        foreign key (antrags_id) references forum_entbannungs_antraege (antrags_id)
);