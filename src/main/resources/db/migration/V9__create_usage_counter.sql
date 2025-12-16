CREATE TABLE usage_counter (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,

    resume_used INT NOT NULL DEFAULT 0,
    jd_used INT NOT NULL DEFAULT 0,
    match_used INT NOT NULL DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_usage_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenant(id),

    CONSTRAINT fk_usage_subscription
        FOREIGN KEY (subscription_id) REFERENCES subscription(id),

    CONSTRAINT uq_usage_subscription UNIQUE (subscription_id)
);
