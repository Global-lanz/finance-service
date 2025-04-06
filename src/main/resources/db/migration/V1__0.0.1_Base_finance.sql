
CREATE TABLE IF NOT EXISTS contract
(
    contract_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_amount       NUMERIC(14, 2),
    contract_type      CHARACTER VARYING,
    frequency          CHARACTER VARYING,
    payment_day        INTEGER,
    start_date         DATE,
    end_date           DATE,
    status             CHARACTER VARYING,
    termination_clause TEXT,
    penalty_fee        NUMERIC(5, 2),
    customer_id        UUID NOT NULL,
    company_id         UUID NOT NULL
    );

CREATE TABLE IF NOT EXISTS invoice
(
    invoice_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    contract_id UUID NOT NULL,
    currency_id UUID,
    amount      NUMERIC(14, 2),
    due_date    DATE,
    description CHARACTER VARYING,
    CONSTRAINT invoice_contract_fk FOREIGN KEY (contract_id) REFERENCES contract (contract_id)
    );

CREATE TABLE IF NOT EXISTS payment
(
    payment_id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    invoice_id   UUID NOT NULL,
    amount       NUMERIC(14, 2),
    payment_date DATE,
    note         CHARACTER VARYING,
    CONSTRAINT payment_invoice_fk FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id)
    );
