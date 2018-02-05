create table "organisation" (
  id bigserial not null,
  name varchar(50) not null,
  address varchar(250) not null,
  last_updated_date timestamp with time zone NOT NULL
    DEFAULT (current_timestamp AT TIME ZONE 'UTC'),
  primary key (id)
);

create table "userdata" (
  id bigserial not null,
  name varchar(50) not null,
  email varchar(50) not null,
  dob date NOT NULL,
  last_updated_date timestamp with time zone NOT NULL
    DEFAULT (current_timestamp AT TIME ZONE 'UTC'),
  org_id bigint not null references organisation (id),
  primary key (id)
);

create table "groupdata" (
  id bigserial not null,
  name varchar(50) not null,
  primary key (id)
);

create table "user_group" (
  user_id bigint not null references userdata (id),
  group_id bigint not null references groupdata (id),
  primary key (user_id, group_id)
);