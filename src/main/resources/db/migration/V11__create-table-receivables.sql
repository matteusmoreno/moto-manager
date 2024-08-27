CREATE TABLE receivables (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_order_id BIGINT NOT NULL,
    value DECIMAL(19, 2) NOT NULL,
    issue_date DATE NOT NULL,
    payment_date DATE,
    status ENUM('PENDING', 'PAID', 'OVERDUE', 'CANCELED') NOT NULL,

    FOREIGN KEY (service_order_id) REFERENCES service_orders(id)
);