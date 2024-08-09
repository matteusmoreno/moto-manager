CREATE TABLE service_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    motorcycle_id BINARY(16),
    seller_id BINARY(16),
    mechanic_id BINARY(16),
    description TEXT,
    labor_price DECIMAL(10, 2),
    total_cost DECIMAL(10, 2),
    service_order_status VARCHAR(50),
    created_at DATETIME,
    started_at DATETIME,
    updated_at DATETIME,
    completed_at DATETIME,
    canceled_at DATETIME,

    FOREIGN KEY (motorcycle_id) REFERENCES motorcycles(id),
    FOREIGN KEY (seller_id) REFERENCES employees(id),
    FOREIGN KEY (mechanic_id) REFERENCES employees(id)
);

    CREATE TABLE service_orders_products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    quantity INT NOT NULL,
    unitary_price DECIMAL(10, 2),
    final_price DECIMAL(10, 2),
    service_order_id BIGINT,

    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (service_order_id) REFERENCES service_orders(id)
);