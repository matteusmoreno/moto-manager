CREATE TABLE motorcycles (
    id BINARY(16) PRIMARY KEY NOT NULL UNIQUE,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(255) NOT NULL,
    color VARCHAR(50) NOT NULL,
    plate VARCHAR(10) NOT NULL UNIQUE,
    year VARCHAR(10) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL
);
