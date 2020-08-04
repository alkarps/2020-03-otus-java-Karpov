create table if not exists user
(
    id   bigint(20) NOT NULL auto_increment primary key,
    name varchar(255),
    age int(3)
);