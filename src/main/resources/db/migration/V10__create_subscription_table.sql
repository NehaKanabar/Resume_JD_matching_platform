CREATE TABLE subscription (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,

    start_date DATE NOT NULL,
    end_date DATE NOT NULL,

    status VARCHAR(20) NOT NULL,
    -- active / expired / cancelled

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_subscription_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenant(id),

    CONSTRAINT fk_subscription_plan
        FOREIGN KEY (plan_id)
        REFERENCES plan(id)
);
