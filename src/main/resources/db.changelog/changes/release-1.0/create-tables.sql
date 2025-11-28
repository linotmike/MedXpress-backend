-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1) app_user
CREATE TABLE IF NOT EXISTS app_user (
                                        id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phone_number   VARCHAR(20)  NOT NULL UNIQUE,
    full_name      VARCHAR(150) NOT NULL,
    email          VARCHAR(150),
    password_hash  VARCHAR(255) NOT NULL,
    role           VARCHAR(30)  NOT NULL,
    is_active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_app_user_role
    CHECK (role IN ('PATIENT','PHARMACY_ADMIN','RIDER','ADMIN'))
    );

CREATE INDEX IF NOT EXISTS idx_app_user_role ON app_user(role);

-- 2) pharmacy
CREATE TABLE IF NOT EXISTS pharmacy (
                                        id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name           VARCHAR(200) NOT NULL,
    license_number VARCHAR(100) NOT NULL,
    phone_number   VARCHAR(20)  NOT NULL,
    email          VARCHAR(150),
    address_line   TEXT         NOT NULL,
    latitude       DOUBLE PRECISION,
    longitude      DOUBLE PRECISION,
    owner_user_id  UUID NOT NULL,
    status         VARCHAR(30) NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_pharmacy_owner_user
    FOREIGN KEY (owner_user_id) REFERENCES app_user(id),
    CONSTRAINT chk_pharmacy_status
    CHECK (status IN ('PENDING_APPROVAL','APPROVED','REJECTED','SUSPENDED')),
    CONSTRAINT uq_pharmacy_license UNIQUE (license_number)
    );

-- 3) medicine (global catalog)
CREATE TABLE IF NOT EXISTS medicine (
                                        id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(200) NOT NULL,
    brand_name  VARCHAR(200),
    form        VARCHAR(30)  NOT NULL,
    strength    VARCHAR(100),
    description TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_medicine_form
    CHECK (form IN ('TABLET','SYRUP','INJECTION','CAPSULE','CREAM','OTHER'))
    );

-- Optional text index for name search (can be added later if you like)
-- CREATE INDEX idx_medicine_name ON medicine
--     USING GIN (to_tsvector('simple', name));

-- 4) pharmacy_medicine (stock + price per pharmacy)
CREATE TABLE IF NOT EXISTS pharmacy_medicine (
                                                 id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pharmacy_id     UUID NOT NULL,
    medicine_id     UUID NOT NULL,
    price           NUMERIC(12,2) NOT NULL,
    stock_quantity  INT NOT NULL DEFAULT 0,
    is_available    BOOLEAN NOT NULL DEFAULT TRUE,
    last_updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_pharmacy_medicine_pharmacy
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacy(id) ON DELETE CASCADE,
    CONSTRAINT fk_pharmacy_medicine_medicine
    FOREIGN KEY (medicine_id) REFERENCES medicine(id) ON DELETE CASCADE,
    CONSTRAINT uq_pharmacy_medicine_unique UNIQUE (pharmacy_id, medicine_id)
    );

CREATE INDEX IF NOT EXISTS idx_pharmacy_medicine_pharmacy
    ON pharmacy_medicine(pharmacy_id);
CREATE INDEX IF NOT EXISTS idx_pharmacy_medicine_medicine
    ON pharmacy_medicine(medicine_id);

-- 5) address (patient delivery addresses)
CREATE TABLE IF NOT EXISTS address (
                                       id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id      UUID NOT NULL,
    label        VARCHAR(100),
    city         VARCHAR(100) NOT NULL,
    sub_city     VARCHAR(100),
    woreda       VARCHAR(50),
    house_number VARCHAR(100),
    directions   TEXT,
    latitude     DOUBLE PRECISION,
    longitude    DOUBLE PRECISION,
    is_default   BOOLEAN NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_address_user
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_address_user ON address(user_id);

-- 6) orders
CREATE TABLE IF NOT EXISTS orders (
                                      id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id     UUID NOT NULL,
    pharmacy_id    UUID NOT NULL,
    address_id     UUID NOT NULL,
    total_amount   NUMERIC(12,2) NOT NULL,
    delivery_fee   NUMERIC(12,2) NOT NULL DEFAULT 0,
    payment_method VARCHAR(30)   NOT NULL,
    status         VARCHAR(40)   NOT NULL,
    created_at     TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_orders_patient
    FOREIGN KEY (patient_id) REFERENCES app_user(id),
    CONSTRAINT fk_orders_pharmacy
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacy(id),
    CONSTRAINT fk_orders_address
    FOREIGN KEY (address_id) REFERENCES address(id),
    CONSTRAINT chk_orders_payment_method
    CHECK (payment_method IN ('CASH_ON_DELIVERY')),
    CONSTRAINT chk_orders_status
    CHECK (status IN (
           'PENDING_PHARMACY_CONFIRMATION',
           'CONFIRMED',
           'REJECTED',
           'ASSIGNED_TO_RIDER',
           'OUT_FOR_DELIVERY',
           'DELIVERED',
           'CANCELLED'
                     ))
    );

CREATE INDEX IF NOT EXISTS idx_orders_patient  ON orders(patient_id);
CREATE INDEX IF NOT EXISTS idx_orders_pharmacy ON orders(pharmacy_id);
CREATE INDEX IF NOT EXISTS idx_orders_status   ON orders(status);

-- 7) order_item
CREATE TABLE IF NOT EXISTS order_item (
                                          id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id             UUID NOT NULL,
    pharmacy_medicine_id UUID NOT NULL,
    quantity             INT NOT NULL,
    unit_price           NUMERIC(12,2) NOT NULL,
    line_total           NUMERIC(12,2) NOT NULL,
    CONSTRAINT fk_order_item_order
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_pharmacy_medicine
    FOREIGN KEY (pharmacy_medicine_id) REFERENCES pharmacy_medicine(id)
    );

CREATE INDEX IF NOT EXISTS idx_order_item_order ON order_item(order_id);

-- 8) delivery
CREATE TABLE IF NOT EXISTS delivery (
                                        id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id      UUID NOT NULL,
    rider_id      UUID NOT NULL,
    status        VARCHAR(30) NOT NULL,
    assigned_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    picked_up_at  TIMESTAMPTZ,
    delivered_at  TIMESTAMPTZ,
    failure_reason TEXT,
    CONSTRAINT fk_delivery_order
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_delivery_rider
    FOREIGN KEY (rider_id) REFERENCES app_user(id),
    CONSTRAINT chk_delivery_status
    CHECK (status IN ('ASSIGNED','PICKED_UP','ON_THE_WAY','DELIVERED','FAILED'))
    );

CREATE INDEX IF NOT EXISTS idx_delivery_order  ON delivery(order_id);
CREATE INDEX IF NOT EXISTS idx_delivery_rider  ON delivery(rider_id);
CREATE INDEX IF NOT EXISTS idx_delivery_status ON delivery(status);
