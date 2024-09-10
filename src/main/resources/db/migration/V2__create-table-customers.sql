CREATE TABLE customers (
    id BINARY(16) PRIMARY KEY NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    birth_date DATE NOT NULL,
    age INT NOT NULL,
    phone VARCHAR(30) NOT NULL UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN
);
