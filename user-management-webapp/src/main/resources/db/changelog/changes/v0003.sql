create table "usercredentials" (
  id bigserial not null,
  login_name varchar(50) not null,
  password varchar(200) not null,
  last_updated_date timestamp with time zone NOT NULL
    DEFAULT (current_timestamp AT TIME ZONE 'UTC'),
  user_id bigint not null references userdata (id),
  primary key (id)
);