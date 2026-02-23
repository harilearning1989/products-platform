CREATE TABLE orders
(
    id             BIGSERIAL PRIMARY KEY,
    customer_email VARCHAR(255),
    status         VARCHAR(50),
    total_amount   NUMERIC(15, 2),
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP
);

CREATE TABLE order_items
(
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    quantity   INT,
    price      NUMERIC(15, 2),
    order_id   BIGINT REFERENCES orders (id)
);

CREATE INDEX idx_order_customer ON orders (customer_email);
CREATE INDEX idx_order_status ON orders (status);