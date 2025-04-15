CREATE TYPE contract_status AS ENUM (
    'QUOTATION',
    'AWAITING_SIGNATURE',
    'APPROVED',
    'RUNNING',
    'TERMINATED',
    'CANCELLED'
);

CREATE TYPE contract_type AS ENUM (
    'QUOTE',
    'AMENDMENT_QUOTE',
    'CANCELLATION_QUOTE',
    'CONTRACT',
    'AMENDMENT_CONTRACT',
    'CANCELLATION_CONTRACT'
);

CREATE TABLE contract_status_transition (
                                            contract_status_transition_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                            from_status CONTRACT_STATUS NOT NULL,
                                            to_status CONTRACT_STATUS NOT NULL,
                                            CONSTRAINT valid_status_transition UNIQUE (from_status, to_status)
);

CREATE TABLE IF NOT EXISTS contract
(
    contract_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_amount        NUMERIC(14, 2),
    "type"              CONTRACT_TYPE,
    frequency           CHARACTER VARYING,
    payment_day         INTEGER,
    "start"             DATE,
    "end"               DATE,
    status              CONTRACT_STATUS,
    termination_clause  TEXT,
    penalty_fee         NUMERIC(5, 2),
    currency_id         UUID,
    customer_id         UUID NOT NULL,
    company_id          UUID NOT NULL
    );

CREATE TABLE IF NOT EXISTS invoice
(
    invoice_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount              NUMERIC(14, 2),
    due_date            DATE,
    description         CHARACTER VARYING,
    contract_id         UUID NOT NULL,
    company_id          UUID NOT NULL,
    CONSTRAINT invoice_contract_fk FOREIGN KEY (contract_id) REFERENCES contract (contract_id)
    );

CREATE TABLE IF NOT EXISTS payment
(
    payment_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    invoice_id          UUID NOT NULL,
    amount              NUMERIC(14, 2),
    payment_date        DATE,
    note                CHARACTER VARYING,
    company_id          UUID NOT NULL,
    CONSTRAINT payment_invoice_fk FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id)
    );
