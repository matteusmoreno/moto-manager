CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    manufacturer VARCHAR(255),
    price DECIMAL(10, 2) DEFAULT 0.00,
    quantity INT NOT NULL DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME,
    active BOOLEAN
);
