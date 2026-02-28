-- Optional: create enum type for payment status
-- Adjust values to match your actual PaymentStatus enum
CREATE TYPE payment_status AS ENUM (
    'INITIATED',
    'SUCCESS',
    'FAILED',
    'CANCELLED'
);

CREATE TABLE payments
(
    id             BIGSERIAL PRIMARY KEY,
    order_id       BIGINT,
    user_id        INTEGER,
    customer_email VARCHAR(255),
    amount         NUMERIC(19, 2),
    status         payment_status,
    transaction_id VARCHAR(255),
    product_id     BIGINT,
    created_at     TIMESTAMP WITH TIME ZONE,
    updated_at     TIMESTAMP WITH TIME ZONE,
    failure_reason TEXT
);

ALTER TABLE payments
ALTER COLUMN status TYPE VARCHAR(50);

DROP TYPE IF EXISTS payment_status;

-- Indexes
CREATE INDEX idx_payment_order ON payments (order_id);
CREATE INDEX idx_payment_status ON payments (status);