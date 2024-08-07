

INSERT INTO addresses (id, zipcode, city, neighborhood, state, street, number, created_at)
VALUES (1, '28994-666', 'Saquarema', 'Bacaxá (Bacaxá)', 'RJ', 'Rua Moacir Picanço', '21', NOW());

INSERT INTO employees (id, username, password, name, email, phone, birth_date, age, cpf, role, address_id, created_at, active)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'admin', 'admin', 'ADMIN MASTER', 'email@email.com', '(22)999999999', '1990-08-28', 33, '999.999.999-99', 'ADMIN', 1, NOW(), TRUE);
