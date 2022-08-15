use gitter_club;
create table user
(
    user_id int auto_increment,
    user_account varchar(255) not null,
    user_password varchar(255) not null,
    user_nickname varchar(255) not null,
    user_birthday date not null,
    user_sex varchar(255) not null,
    user_follow_repositories varchar(255) null,
    constraint user_pk
        primary key (user_id)
);

