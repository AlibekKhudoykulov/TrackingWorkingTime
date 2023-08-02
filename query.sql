create database if not exists trackingworkinghours;

create table employee
(
    id         int auto_increment
        primary key,
    first_name varchar(50) not null,
    last_name  varchar(50) not null
);

create table project
(
    id          int auto_increment
        primary key,
    name        varchar(100) not null,
    description text         null
);

create table working_time_record
(
    id           int auto_increment
        primary key,
    employee_id  int    not null,
    project_id   int    not null,
    start_time   time   null,
    end_time     time   null,
    date         date   null,
    hours_worked double null,
    constraint working_time_record_ibfk_1
        foreign key (employee_id) references employee (id),
    constraint working_time_record_ibfk_2
        foreign key (project_id) references project (id)
);

create index employee_id
    on working_time_record (employee_id);

create index project_id
    on working_time_record (project_id);