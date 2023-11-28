create table profile
(
    id    serial primary key,
    name  text not null,
    password text  not null,
    email text not null,
    email_confirm boolean,
    role text not null
);
-- "Password1_loungy_2023"
insert into profile(name, password, email, email_confirm, role) values ('Alpha', '1c3afbc5c8159bee03a0ac804e99640536b433de01ba2340173c3b99d2fa88f9', 'alpha1@mail.com', true, 'ADMIN');
insert into profile(name, password,email, email_confirm, role) values ('Beta', 'beta2', 'beta2@mail.com', true, 'USER');
insert into profile(name, password,email, email_confirm, role) values ('Charlie', 'charlie3', 'charlie3@mail.com', true, 'USER');


create table token
(
    token       text primary key,
    signing_key text      not null,
    valid_till  timestamp not null,
    created_at  timestamp not null
);


-- password_recovery
-- create table videos :: audios :: images
-- (id, file_name, created_at, likes, mime_type, data)

create table images
(
    id    serial primary key,
    file_name  text not null,
    mime_type text not null,
    data bytea not null
--     created_at  timestamp not null,
--     likes int not null,
);

create table email_confirm
(
    id    serial primary key,
    valid_till  timestamp not null,
    created_at  timestamp not null,
    key text not null,
    email text not null
);