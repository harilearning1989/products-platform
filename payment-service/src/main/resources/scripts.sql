CREATE TABLE payments
(
    id             BIGSERIAL PRIMARY KEY,
    order_id       BIGINT NOT NULL,
    customer_email VARCHAR(255),
    amount         NUMERIC(15, 2),
    status         VARCHAR(50),
    transaction_id VARCHAR(255),
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP
);

CREATE INDEX idx_payment_order ON payments (order_id);
CREATE INDEX idx_payment_status ON payments (status);