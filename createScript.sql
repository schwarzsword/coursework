create table users (
  id       serial primary key,
  name     varchar(50)         not null,
  surname  varchar(50)         not null,
  username varchar(255) unique  not null,
  password varchar(100)        not null,
  mail     varchar(255) unique not null
);

create table roles (
  id   serial primary key,
  role varchar(20) not null
);

create table certificate (
  id     serial primary key,
  date   timestamp                                        not null,
  expert integer check (expert > 0) references users (id) not null
);

create table painting (
  id          serial primary key,
  name        varchar(50) not null,
  author      varchar(50) not null,
  technique   varchar(30) not null,
  genre       varchar(30) not null,
  description text,
  img         varchar(255),
  certificate integer unique check (certificate > 0) references certificate (id)
);

create table wallet (
  id      serial primary key,
  owner   integer unique              not null check (owner > 0) references users (id),
  balance bigint check (balance > -1) not null
);

create table lot (
  id          serial primary key,
  painting    integer check (painting > 0) references painting (id)                                     not null,
  start_date  timestamp,
  state       varchar(50)                                                                               not null,
  start_price bigint check (start_price > 0)                                                            not null,
  seller      integer check (seller > 0) references users (id)                                          not null,
  last_bet    integer check (seller > 0) references users (id),
  policy      integer check (policy > 0 and policy < 8)
);

create table end_date (
  id             serial primary key,
  lot            integer unique not null check (lot > 0) references lot (id),
  expecting_date timestamp      not null,
  state          boolean        not null
);

create table payment (
  id          serial primary key,
  source      integer                not null check (source > 0) references wallet (id),
  destination integer                not null check (destination > 0) references wallet (id),
  sum         bigint check (sum > 0) not null
);

create table deal (
  id        serial primary key,
  sold_date integer unique check (sold_date > 0) references end_date (id)                            not null,
  customer  integer check (customer > 0) references users (id)                                       not null,
  payment   integer check (payment >
                           0) references payment (id) unique                                         not null                                                                                        not null
);

create table persistent_logins (
  username  varchar(100) not null,
  series    varchar(64) primary key,
  token     varchar(64)  not null,
  last_used timestamp    not null
);

create table users_roles (
  id       serial primary key,
  username integer references users (id) not null,
  role     integer references roles (id) not null
);

insert into roles (id, role)
values (1, 'ROLE_USER');
insert into roles (id, role)
values (2, 'ROLE_ADMIN');
insert into roles (id, role)
values (3, 'ROLE_EXPERT');
insert into roles (id, role)
values (4, 'ROLE_BANNED');

