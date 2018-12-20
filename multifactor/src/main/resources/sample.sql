CREATE DATABASE mydb; -- mydb �����ͺ��̽� ����
USE mydb; -- �⺻ �����ͺ��̽��� mydb�� ����
CREATE TABLE mytable ( id INT PRIMARY KEY, name VARCHAR(20) ); -- mytable ���̺� ����
INSERT INTO mytable VALUES ( 1, 'Will' ); -- ������ �Է�
INSERT INTO mytable VALUES ( 2, 'Marry' ); 
INSERT INTO mytable VALUES ( 3, 'Dean' );
SELECT id, name FROM mytable WHERE id = 1;
UPDATE mytable SET name = 'Willy' WHERE id = 1;
SELECT id, name FROM mytable;
DELETE FROM mytable WHERE id = 1;
SELECT id, name FROM mytable;
DROP DATABASE mydb;
SELECT count(1) from mytable; gives the number of records in the table