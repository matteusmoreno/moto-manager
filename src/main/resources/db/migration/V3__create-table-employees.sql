CREATE TABLE employees (
    id BINARY(16) PRIMARY KEY NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(30) UNIQUE,
    birth_date DATE,
    age INTEGER,
    cpf VARCHAR(14) UNIQUE,
    role VARCHAR(50),
    address_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN,

    FOREIGN KEY (address_id) REFERENCES addresses(id)
);
