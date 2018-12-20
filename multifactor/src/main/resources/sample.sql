CREATE DATABASE mydb; -- mydb 데이터베이스 생성
USE mydb; -- 기본 데이터베이스로 mydb를 지정
CREATE TABLE mytable ( id INT PRIMARY KEY, name VARCHAR(20) ); -- mytable 테이블 생성
INSERT INTO mytable VALUES ( 1, 'Will' ); -- 데이터 입력
INSERT INTO mytable VALUES ( 2, 'Marry' ); 
INSERT INTO mytable VALUES ( 3, 'Dean' );
SELECT id, name FROM mytable WHERE id = 1;
UPDATE mytable SET name = 'Willy' WHERE id = 1;
SELECT id, name FROM mytable;
DELETE FROM mytable WHERE id = 1;
SELECT id, name FROM mytable;
DROP DATABASE mydb;
SELECT count(1) from mytable; gives the number of records in the table