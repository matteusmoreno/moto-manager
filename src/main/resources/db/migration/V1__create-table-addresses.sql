CREATE TABLE addresses (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    zipcode VARCHAR(20) NOT NULL,
    city VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(255),
    state VARCHAR(2) NOT NULL,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(10),
    complement VARCHAR(255),
    created_at TIMESTAMP
);
