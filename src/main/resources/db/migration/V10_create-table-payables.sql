CREATE TABLE payables (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id BIGINT NOT NULL,
    description VARCHAR(255),
    value DECIMAL(19, 2) NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    payment_date DATE,
    status ENUM('PENDING', 'PAID', 'OVERDUE', 'CANCELED') NOT NULL,

    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);
