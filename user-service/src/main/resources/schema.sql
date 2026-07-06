create table if not exists t_user
(
    id       bigint primary key auto_increment,
    username varchar(64)  not null unique,
    password varchar(128) not null,
    role     varchar(32)  not null
);
