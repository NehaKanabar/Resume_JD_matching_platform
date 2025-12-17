CREATE TABLE invoice (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,

    amount NUMERIC(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL, -- PENDING / PAID / FAILED

    due_date DATE NOT NULL,
    pdf_url TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_invoice_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenant(id),

    CONSTRAINT fk_invoice_subscription
        FOREIGN KEY (subscription_id) REFERENCES subscription(id)
);
