CREATE TABLE users
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(40) UNIQUE NOT NULL,
    location VARCHAR(100)       NOT NULL,
    email    VARCHAR(100)       NOT NULL
);