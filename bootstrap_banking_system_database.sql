CREATE TABLE bank (
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
bic VARCHAR(15) NOT NULL UNIQUE,
name VARCHAR(50) NOT NULL
);

CREATE TABLE user (
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(255) NOT NULL UNIQUE,
full_name VARCHAR(255),
phone_number VARCHAR(20)
);

CREATE TABLE bank_account (
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
bank_id INT,
iban VARCHAR(40) NOT NULL ,
card_type VARCHAR(6) NOT NULL,
user_id INT,
balance REAL NOT NULL,
CONSTRAINT FK_account_to_bank FOREIGN KEY (bank_id) REFERENCES bank(id),
CONSTRAINT FK_account_to_user FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE credit (
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
credit_initialize_timestamp TIMESTAMP,
bank_account_id INT ,
amount REAL NOT NULL,
CONSTRAINT FK_credit_to_account FOREIGN KEY (bank_account_id) REFERENCES bank_account(id)
);


CREATE TABLE transaction (
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
sender_bank_account_id INT,
receiver_bank_account_id INT,
amount REAL NOT NULL,
timestamp TIMESTAMP,
CONSTRAINT FK_transaction_to_sender FOREIGN KEY (sender_bank_account_id) REFERENCES bank_account(id),
CONSTRAINT FK_transaction_to_receiver FOREIGN KEY (receiver_bank_account_id) REFERENCES bank_account(id)
);




-- POPULATE USERS
INSERT INTO user (username, full_name, phone_number) VALUES ("king", "Jon Snow", "+37061122333");
INSERT INTO user (username, full_name, phone_number) VALUES ("ironlady", "Angela Merkel", "+37061188555");
INSERT INTO user (username, full_name, phone_number) VALUES ("model", "Brigitte Jones", "+37065599444");
INSERT INTO user (username, full_name, phone_number) VALUES ("funnyguy", "Chandler Bing", "+37060000777");
INSERT INTO user (username, full_name, phone_number) VALUES ("troublemaker", "Bart Simpson", "+37062211777");

-- POPULATE BANKS
INSERT INTO bank (name, bic) VALUES ("Swedbank", "HABALT22");
INSERT INTO bank (name, bic) VALUES ("Danske Bank", "SMPOLT22");
INSERT INTO bank (name, bic) VALUES ("SEB", "CBVILT2X");
INSERT INTO bank (name, bic) VALUES ("Luminor", "AGBLLT2X");
INSERT INTO bank (name, bic) VALUES ("Šiaulių Bankas", "CBSBLT26");

-- POPULATE BANK ACCOUNTS
-- swedbank
INSERT INTO bank_account (bank_id, iban, card_type, user_id, balance) VALUES (1, "LT607300011111111101", "CREDIT", 1, 0.0);
INSERT INTO bank_account (bank_id, iban, card_type, user_id, balance) VALUES (1, "LT507300011111111102", "DEBIT", 1, 1050.52);
-- danske
INSERT INTO bank_account (bank_id, iban, card_type, user_id, balance) VALUES (2, "LT607400022222222201", "CREDIT", 2, 932.13);
INSERT INTO bank_account (bank_id, iban, card_type, user_id, balance) VALUES (2, "LT507300011111111102", "DEBIT", 3, 166.28);
-- seb
INSERT INTO bank_account (bank_id, iban, card_type, user_id, balance) VALUES (3, "LT607044033333333301", "CREDIT", 4, 3017.14);
INSERT INTO bank_account (bank_id, iban, card_type, user_id, balance) VALUES (3, "LT507300011111111102", "DEBIT", 5, 600.00);
-- luminor
INSERT INTO bank_account (bank_id, iban, card_type, user_id, balance) VALUES (4, "LT604010044444444401", "CREDIT", 3, -100.0);
INSERT INTO bank_account (bank_id, iban, card_type, user_id, balance) VALUES (4, "LT507300011111111102", "DEBIT", 2, 2.21);
-- šiaulių
INSERT INTO bank_account (bank_id, iban, card_type, user_id, balance) VALUES (5, "LT607180055555555501", "CREDIT", 5, 2014.65);
INSERT INTO bank_account (bank_id, iban, card_type, user_id, balance) VALUES (5, "LT507300011111111102", "DEBIT", 1, 0.0);

-- POPULATE CREDITS
INSERT INTO credit (credit_initialize_timestamp, bank_account_id, amount) VALUES (NULL, 1, 0.0);
INSERT INTO credit (credit_initialize_timestamp, bank_account_id, amount) VALUES (NULL, 3, 0.0);
INSERT INTO credit (credit_initialize_timestamp, bank_account_id, amount) VALUES (NULL, 5, 0.0);
INSERT INTO credit (credit_initialize_timestamp, bank_account_id, amount) VALUES ("2020-08-20 14:23:56", 7, -100.00);
INSERT INTO credit (credit_initialize_timestamp, bank_account_id, amount) VALUES (NULL, 9, 0.0);


-- POPULATE TRANSACTIONS
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (3, 2, 1050.52, "2020-09-04 14:35:13");
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (2, 3, 932.13, "2020-09-05 16:45:10");
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (7, 4, 166.28, "2020-09-16 19:59:08");
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (1, 5, 17.14, "2020-08-02 10:13:27");
-- sender "null" indicates top-up operation
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (null, 5, 1000.00, "2020-07-01 08:37:02");
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (null, 5, 1000.00, "2020-08-01 08:42:30");
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (null, 5, 1000.00, "2020-08-01 08:41:14");
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (null, 6, 600.00, "2020-09-01 12:11:01");
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (7, 6, 110.00, "2020-08-20 14:23:56");
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (6, 8, 2.21, "2020-09-26 23:42:58");
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (5, 9, 2014.65, "2020-09-11 17:33:32");
-- receiver "null" indicates withdraw operation
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (10, null, 300.00, "2020-09-28 13:02:41");
INSERT INTO transaction (sender_bank_account_id, receiver_bank_account_id, amount, timestamp) VALUES (1, null, 60.00, "2020-09-17 15:30:03");