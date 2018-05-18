CREATE TABLE Borrowers (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
borrower_name VARCHAR(30)
);
INSERT INTO Borrowers (borrower_name) VALUES ('First Borrower');
INSERT INTO Borrowers (borrower_name) VALUES ('Second Borrower');
INSERT INTO Borrowers (borrower_name) VALUES ('Third Person');
INSERT INTO Borrowers (borrower_name) VALUES ('Fourth Borrower');
INSERT INTO Borrowers (borrower_name) VALUES ('Fifth Person');
SELECT * FROM Borrowers