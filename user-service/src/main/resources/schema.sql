create table if not exists t_user
(
    id       bigint primary key auto_increment,
    username varchar(64)  not null unique,
    password varchar(128) not null,
    role     varchar(32)  not null
);

create table if not exists t_order
(
    id           bigint primary key auto_increment,
    user_id      bigint         not null,
    product_code varchar(64)    not null,
    quantity     int            not null,
    amount       decimal(10, 2) not null,
    status       varchar(16)    not null,
    created_at   timestamp      not null
);

create table if not exists t_message_event
(
    id         bigint primary key auto_increment,
    event_type varchar(64) not null,
    payload    text        not null,
    created_at timestamp   not null
);

create table if not exists t_product_stock
(
    id           bigint primary key auto_increment,
    product_code varchar(64) not null unique,
    stock        int         not null,
    updated_at   timestamp   not null default current_timestamp on update current_timestamp
);

insert into t_product_stock (product_code, stock)
select 'P001', 100 where not exists (select 1 from t_product_stock where product_code = 'P001');
insert into t_product_stock (product_code, stock)
select 'P002', 50  where not exists (select 1 from t_product_stock where product_code = 'P002');

create table if not exists undo_log
(
    branch_id     bigint       not null,
    xid           varchar(128) not null,
    context       varchar(128) not null,
    rollback_info longblob     not null,
    log_status    int          not null,
    log_created   datetime(6)  not null,
    log_modified  datetime(6)  not null,
    unique key ux_undo_log (xid, branch_id)
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4;
