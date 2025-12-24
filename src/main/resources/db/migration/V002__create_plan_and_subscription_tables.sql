-- =========================
-- PLAN TABLE
-- =========================
CREATE TABLE plan (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    resume_limit INT NOT NULL,
    jd_limit INT NOT NULL,
    match_limit INT NOT NULL,
    price_monthly NUMERIC(10,2) NOT NULL,
    price_yearly NUMERIC(10,2) NOT NULL
);

-- =========================
-- SUBSCRIPTION TABLE
-- =========================
CREATE TABLE subscription (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,

    CONSTRAINT fk_subscription_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenant(id),

    CONSTRAINT fk_subscription_plan
        FOREIGN KEY (plan_id) REFERENCES plan(id)
);
