CREATE TABLE notifications
(
    id          BIGSERIAL PRIMARY KEY,
    event_id    VARCHAR(255) NOT NULL UNIQUE,
    type        VARCHAR(100) NOT NULL,
    recipient   VARCHAR(255) NOT NULL,
    channel     VARCHAR(50)  NOT NULL,
    content     TEXT         NOT NULL,
    status      VARCHAR(50)  NOT NULL,
    retry_count INT                   DEFAULT 0,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at     TIMESTAMP
);

CREATE INDEX idx_notification_event_id ON notifications (event_id);
CREATE INDEX idx_notification_status ON notifications (status);