CREATE TABLE payment (
    id BIGSERIAL PRIMARY KEY,

    invoice_id BIGINT NOT NULL,
    tenant_id BIGINT NOT NULL,

    stripe_payment_intent_id VARCHAR(100),
    amount NUMERIC(10,2) NOT NULL,

    status VARCHAR(20) NOT NULL, -- SUCCESS / FAILED
    raw_response JSONB,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_invoice
        FOREIGN KEY (invoice_id) REFERENCES invoice(id),

    CONSTRAINT fk_payment_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenant(id)
);
