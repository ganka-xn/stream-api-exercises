-- Создание таблиц с именами, которые используются в data.sql и моделях
CREATE TABLE IF NOT EXISTS customers
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    tier INTEGER
);

CREATE TABLE IF NOT EXISTS products
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255),
    category VARCHAR(255),
    price    DOUBLE
);

CREATE TABLE IF NOT EXISTS product_orders
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_date    DATE,
    delivery_date DATE,
    status        VARCHAR(50),
    customer_id   BIGINT
);

CREATE TABLE IF NOT EXISTS order_product_relationship
(
    order_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (order_id, product_id)
);

-- Внешние ключи
ALTER TABLE product_orders
    ADD CONSTRAINT fk_product_orders_customer
        FOREIGN KEY (customer_id) REFERENCES customers (id);

ALTER TABLE order_product_relationship
    ADD CONSTRAINT fk_order_product_order
        FOREIGN KEY (order_id) REFERENCES product_orders (id);

ALTER TABLE order_product_relationship
    ADD CONSTRAINT fk_order_product_product
        FOREIGN KEY (product_id) REFERENCES products (id);

