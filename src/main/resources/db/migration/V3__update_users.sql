alter table users add account_non_expired BIT(1) DEFAULT 1;
alter table users add account_non_locked BIT(1) DEFAULT 1;
alter table users add credentials_non_expired BIT(1) DEFAULT 1;
alter table users add enabled BIT(1) DEFAULT 1;