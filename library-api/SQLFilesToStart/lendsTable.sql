CREATE TABLE Lends(
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
book_Id INT(6),
borrower_Id INT(6)
);
INSERT INTO Lends (book_Id, borrower_Id) VALUES ('8', '1');
INSERT INTO Lends (book_Id, borrower_Id) VALUES ('5', '5');
INSERT INTO Lends (book_Id, borrower_Id) VALUES ('3', '3');
INSERT INTO Lends (book_Id, borrower_Id) VALUES ('1', '2');
INSERT INTO Lends (book_Id, borrower_Id) VALUES ('4', '4');
SELECT * FROM Lends