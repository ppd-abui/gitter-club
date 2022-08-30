create table gitter_club.pull_request
(
    pull_request_id            int auto_increment
        primary key,
    pull_request_repo_owner    varchar(255)                  not null,
    pull_request_repo_name     varchar(255)                  not null,
    pull_request_remote_branch varchar(255) default 'master' not null,
    pull_request_local_branch  varchar(255) default 'master' not null,
    pull_request_creator       varchar(255)                  not null,
    pull_request_theme         varchar(255)                  not null
);