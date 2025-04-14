
CREATE TABLE IF NOT EXISTS contract
(
    contract_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_amount        NUMERIC(14, 2),
    type                CHARACTER VARYING,
    frequency           CHARACTER VARYING,
    payment_day         INTEGER,
    start               DATE,
    end                 DATE,
    status              CHARACTER VARYING,
    termination_clause  TEXT,
    penalty_fee         NUMERIC(5, 2),
    currency_id         UUID,
    customer_id         UUID NOT NULL,
    company_id          UUID NOT NULL
    );

CREATE TABLE IF NOT EXISTS invoice
(
    invoice_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    contract_id         UUID NOT NULL,
    amount              NUMERIC(14, 2),
    due_date            DATE,
    description         CHARACTER VARYING,
    company_id         UUID NOT NULL
    CONSTRAINT invoice_contract_fk FOREIGN KEY (contract_id) REFERENCES contract (contract_id)
    );

CREATE TABLE IF NOT EXISTS payment
(
    payment_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    invoice_id          UUID NOT NULL,
    amount              NUMERIC(14, 2),
    payment_date        DATE,
    note                CHARACTER VARYING,
    company_id         UUID NOT NULL
    CONSTRAINT payment_invoice_fk FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id)
    );

CREATE TYPE contract_status AS ENUM (
    'QUOTATION',
    'AWAITING_SIGNATURE',
    'APPROVED',
    'RUNNING',
    'TERMINATED',
    'CANCELLED'
);

CREATE TABLE contract_status_transition (
    contract_status_transition_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    from_status contract_status NOT NULL,
    to_status contract_status NOT NULL,
    CONSTRAINT valid_status_transition UNIQUE (from_status, to_status)
);