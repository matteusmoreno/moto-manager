CREATE TABLE suppliers(
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,
    name VARCHAR(255),
    phone VARCHAR(255),
    email VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL
);