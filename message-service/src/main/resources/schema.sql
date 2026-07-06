create table if not exists t_message_event
(
    id         bigint primary key auto_increment,
    event_type varchar(64) not null,
    payload    text        not null,
    created_at timestamp   not null
);
