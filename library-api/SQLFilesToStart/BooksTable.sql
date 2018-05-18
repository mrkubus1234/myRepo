CREATE TABLE Books (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
title VARCHAR(60),
year INT(4),
author_Id VARCHAR(50)
);
INSERT INTO Books (title, year, author_Id) VALUES ('Book1', '2005', '1');
INSERT INTO Books (title, year, author_Id) VALUES ('Book2', '2010', '2');
INSERT INTO Books (title, year, author_Id) VALUES ('Book3', '2017', '3');
INSERT INTO Books (title, year, author_Id) VALUES ('Book4', '2018', '7');
INSERT INTO Books (title, year, author_Id) VALUES ('Book5', '2000', '5');
INSERT INTO Books (title, year, author_Id) VALUES ('Book6', '1111', '6');
INSERT INTO Books (title, year, author_Id) VALUES ('Book7', '1990', '7');
INSERT INTO Books (title, year, author_Id) VALUES ('Book8', '1900', '4');
INSERT INTO Books (title, year, author_Id) VALUES ('Book5', '2000', '5');
SELECT * FROM Books