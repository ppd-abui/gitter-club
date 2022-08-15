create table user
(
    userId int auto_increment,
    user_account varchar(255) not null,
    user_password varchar(255) not null,
    user_nickname varchar(255) not null,
    user_birthday date not null,
    user_sex varchar(255) not null,
    user_follow_repositories varchar(255) null,
    constraint user_pk
        primary key (userId)
);

