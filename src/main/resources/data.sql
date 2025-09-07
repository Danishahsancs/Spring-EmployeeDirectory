-- Create this file as src/main/resources/data.sql
-- Delete or rename your import.sql file

-- Insert Departments first (without managers)
INSERT INTO DEPARTMENT (DPT_NAME) VALUES ('Engineering');
INSERT INTO DEPARTMENT (DPT_NAME) VALUES ('Sales');
INSERT INTO DEPARTMENT (DPT_NAME) VALUES ('Recruiting');

-- Insert Employees
INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, TITLE, PHONE, EMAIL, HIRE_DATE, MANAGER_EMP_NUM, DPT_NUM) 
VALUES ('Alice', 'Anders', 'Director of Engineering', '555-1000', 'alice@corp.com', '2016-01-10', NULL, 1);

INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, TITLE, PHONE, EMAIL, HIRE_DATE, MANAGER_EMP_NUM, DPT_NUM) 
VALUES ('Eli', 'Evans', 'VP Sales', '555-2000', 'eli@corp.com', '2015-03-12', NULL, 2);

INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, TITLE, PHONE, EMAIL, HIRE_DATE, MANAGER_EMP_NUM, DPT_NUM) 
VALUES ('Hana', 'Hughes', 'Recruiter', '555-3000', 'hana@corp.com', '2022-09-01', NULL, 3);

INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, TITLE, PHONE, EMAIL, HIRE_DATE, MANAGER_EMP_NUM, DPT_NUM) 
VALUES ('Bob', 'Baker', 'Engineering Manager', '555-1001', 'bob@corp.com', '2017-02-15', 1, 1);

INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, TITLE, PHONE, EMAIL, HIRE_DATE, MANAGER_EMP_NUM, DPT_NUM) 
VALUES ('Cara', 'Cole', 'Senior Engineer', '555-1002', 'cara@corp.com', '2019-04-01', 2, 1);

INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, TITLE, PHONE, EMAIL, HIRE_DATE, MANAGER_EMP_NUM, DPT_NUM) 
VALUES ('Derek', 'Diaz', 'Engineer', '555-1003', 'derek@corp.com', '2020-06-20', 2, 1);

INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, TITLE, PHONE, EMAIL, HIRE_DATE, MANAGER_EMP_NUM, DPT_NUM) 
VALUES ('Fay', 'Foster', 'Sales Manager', '555-2001', 'fay@corp.com', '2018-08-01', 3, 2);

INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, TITLE, PHONE, EMAIL, HIRE_DATE, MANAGER_EMP_NUM, DPT_NUM) 
VALUES ('Gus', 'Green', 'Account Exec', '555-2002', 'gus@corp.com', '2021-01-15', 4, 2);

-- Update departments with managers (using auto-generated IDs)
UPDATE DEPARTMENT SET MANAGER_EMP_NUM = 1 WHERE DPT_NAME = 'Engineering';
UPDATE DEPARTMENT SET MANAGER_EMP_NUM = 2 WHERE DPT_NAME = 'Sales';  
UPDATE DEPARTMENT SET MANAGER_EMP_NUM = 3 WHERE DPT_NAME = 'Recruiting';