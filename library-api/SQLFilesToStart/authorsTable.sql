CREATE TABLE Authors (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(30)
);
INSERT INTO Authors (name) VALUES ('First Author');
INSERT INTO Authors (name) VALUES ('Second Author');
INSERT INTO Authors (name) VALUES ('Third Author');
INSERT INTO Authors (name) VALUES ('Fourth Kowalski');
INSERT INTO Authors (name) VALUES ('Fifth Nowak');
INSERT INTO Authors (name) VALUES ('Six Kowal');
INSERT INTO Authors (name) VALUES ('Jan Author');
SELECT * FROM Authors