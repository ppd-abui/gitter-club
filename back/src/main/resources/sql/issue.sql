create table gitter_club.issue
(
    issue_id       int auto_increment
        primary key,
    issue_title    varchar(255) null,
    issue_content  varchar(255) null,
    issue_username varchar(255) null,
    issue_reponame varchar(255) null,
    issue_creator varchar(255) null
);