CREATE TABLE payment_history
(
    id             BIGSERIAL PRIMARY KEY,
    payment_id     BIGINT,
    order_id       BIGINT,
    customer_email VARCHAR(255),
    amount         NUMERIC(15, 2),
    status         VARCHAR(50),
    transaction_id VARCHAR(255),
    event_time     TIMESTAMP
);

CREATE INDEX idx_ph_order ON payment_history (order_id);
CREATE INDEX idx_ph_email ON payment_history (customer_email);
CREATE INDEX idx_ph_status ON payment_history (status);