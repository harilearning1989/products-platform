CREATE TABLE inventory
(
    id                 BIGSERIAL PRIMARY KEY,
    product_id         BIGINT    NOT NULL UNIQUE,
    available_quantity INT       NOT NULL CHECK (available_quantity >= 0),
    reserved_quantity  INT       NOT NULL DEFAULT 0 CHECK (reserved_quantity >= 0),
    version            INT,
    last_updated       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_inventory_product_id ON inventory (product_id);

CREATE TABLE inventory
(
    id                 BIGSERIAL PRIMARY KEY,
    product_id         BIGINT    NOT NULL,
    warehouse_id       BIGINT    NOT NULL,
    available_quantity INT       NOT NULL,
    reserved_quantity  INT       NOT NULL DEFAULT 0,
    version            INT,
    last_updated       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_id, warehouse_id)
);