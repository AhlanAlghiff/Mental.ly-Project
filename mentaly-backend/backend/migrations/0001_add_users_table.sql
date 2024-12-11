create table if not exists users (
    id uuid primary key,
    name varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    refresh_token text,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);