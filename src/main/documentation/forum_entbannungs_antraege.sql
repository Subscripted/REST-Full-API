create table forum_entbannungs_antraege
(
    user_id      bigint null,
    teamler_id   bigint null,
    status       tinyint(1)   null,
    antrag_title varchar(255) null,
    antrags_id   bigint not null
        primary key,
    constraint FKg4kqcb42il5hk85cnw0p2gqsj
        foreign key (teamler_id) references forum_user (user_id),
    constraint FKrcei3707ghfncngmj0mno1owc
        foreign key (user_id) references forum_user (user_id)
);