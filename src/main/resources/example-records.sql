CREATE TABLE IF NOT EXISTS test_schema.user_test (
  user_id varchar(36) not null primary key,
  first_name varchar(100) not null,
  last_name varchar (100) not null,
  email varchar(100) not null 	
);

insert into test_schema.user_test(user_id, first_name, last_name, email)
VALUES('0001', 'first name 1', 'last name 1', 'email1@example.com'),
('0002', 'first name 2', 'last name 2', 'email2@example.com'),('0003', 'first name 3', 'last name 3', 'email3@example.com'),
('0004', 'first name 4', 'last name 4', 'email4@example.com');