CREATE TABLE contract_status_transition (
    contract_status_transition_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    from_status CHARACTER VARYING NOT NULL,
    to_status CHARACTER VARYING NOT NULL,
    CONSTRAINT valid_status_transition UNIQUE (from_status, to_status)
);

CREATE TABLE IF NOT EXISTS contract
(
    contract_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_amount        NUMERIC(14, 2),
    contract_type       CHARACTER VARYING,
    frequency           CHARACTER VARYING,
    payment_day         INTEGER,
    start_date          DATE,
    end_date            DATE,
    contract_status     CHARACTER VARYING,
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
    note                TEXT,
    company_id          UUID NOT NULL,
    CONSTRAINT payment_invoice_fk FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id)
    );
