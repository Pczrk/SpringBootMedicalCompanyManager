create table patients(
    patient_id serial8 unique not null,
    pesel char(11) unique not null,
    first_name varchar(30) not null,
    last_name varchar(70) not null,
    birthdate date not null,
    street varchar(70) not null,
    city varchar(30) not null,
    zip_code char(6) not null,
    phone_number varchar(30),
    password varchar(300),
    primary key (patient_id)
);

create table patients_sessions(
    session_id char(32) unique not null,
    patient_id int8 not null,
    expiration_date timestamp not null,
    primary key (session_id),
    foreign key (patient_id) references patients(patient_id)
);

create table institutions(
    institution_id serial unique not null,
    name varchar(70) not null,
    street varchar(70) not null,
    city varchar(30) not null,
    zip_code char(6) not null,
    primary key (institution_id)
);

create table employees(
      employee_id serial unique not null,
      institution_id int not null,
      mail varchar(320) unique not null,
      password varchar(300) not null,
      first_name varchar(30) not null,
      last_name varchar(70) not null,
      permission_level int2 not null,
      primary key (employee_id),
      foreign key (institution_id) references institutions(institution_id)
);

create table medical_examinations(
    examination_id serial8 unique not null,
    patient_id int8 not null,
    institution_id int not null,
    employee_id int not null,
    examination_date date not null,
    details varchar(900),
    primary key (examination_id),
    foreign key (patient_id) references patients(patient_id),
    foreign key (institution_id) references institutions(institution_id),
    foreign key (employee_id) references employees(employee_id)
);

create table register_codes(
    register_code char(8) unique not null,
    institution_id int not null,
    permission_level int2 not null,
    expiration_date timestamp not null,
    primary key (register_code),
    foreign key (institution_id) references institutions(institution_id)
);

create table employees_sessions(
    session_id char(32) unique not null,
    employee_id int not null,
    expiration_date timestamp not null,
    primary key (session_id),
    foreign key (employee_id) references employees(employee_id)
);

insert into institutions(institution_id,name,street,city,zip_code) values(1,'Institution 1','Street 1','City 1','25-001');
insert into institutions(institution_id,name,street,city,zip_code) values(2,'Institution 2','Street 2','City 2','27-002');
insert into institutions(institution_id,name,street,city,zip_code) values(3,'Institution 3','Street 3','City 3','28-003');
insert into register_codes(register_code, institution_id, permission_level, expiration_date) values('superUSR',1,3,'2026-01-01 00:00:00');