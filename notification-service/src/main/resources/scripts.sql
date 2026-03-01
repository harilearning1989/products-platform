CREATE TABLE notifications
(
    id             BIGSERIAL PRIMARY KEY,
    order_id       BIGINT       NOT NULL,
    email          VARCHAR(150) NOT NULL,
    type           VARCHAR(50)  NOT NULL,
    status         VARCHAR(30)  NOT NULL,
    failure_reason VARCHAR(255),
    created_at     TIMESTAMP    NOT NULL,
    sent_at        TIMESTAMP
);

CREATE INDEX idx_notifications_order_id
    ON notifications (order_id);

CREATE INDEX idx_notifications_status
    ON notifications (status);

CREATE INDEX idx_notifications_created_at
    ON notifications (created_at);