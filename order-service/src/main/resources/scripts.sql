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

====================================

CREATE TABLE order_product
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,
    quantity   INTEGER        NOT NULL CHECK (quantity > 0),
    amount     NUMERIC(12, 2) NOT NULL CHECK (amount >= 0),
    status     VARCHAR(20)    NOT NULL
        CHECK (status IN ('CREATED', 'RESERVED', 'CONFIRMED', 'CANCELLED')),
    created_at TIMESTAMPTZ    NOT NULL,
    updated_at TIMESTAMPTZ    NOT NULL
);

CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_product_id ON orders(product_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);