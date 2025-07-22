ALTER TABLE IF EXISTS contract
    ADD COLUMN week_payment_day CHARACTER VARYING;

UPDATE contract SET frequency = 'MONTHLY' WHERE frequency NOT IN ('ONLY_ONCE', 'WEEKLY', 'ANNUALLY');