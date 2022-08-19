use gitter_club;
create table repository
(
    repo_id int auto_increment comment '仓库id',
    repo_owner varchar(255) not null comment '所有者',
    repo_name varchar(255) not null comment '仓库名',
    repo_bio varchar(255) null comment '仓库简介',
    repo_visibility varchar(255) not null comment '仓库可见性',
    repo_followers varchar(255) null comment '仓库关注者',
    repo_issues varchar(255) null comment '本仓库拥有的工单',
    repo_collaborators varchar(255) null,
    constraint repository_pk
        primary key (repo_id)
);

