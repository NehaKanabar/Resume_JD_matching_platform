CREATE TABLE tenant (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    stripe_customer_id VARCHAR(255),

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    disabled BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT fk_user_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenant(id)
);

CREATE TABLE plan (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    version INT NOT NULL,

    resume_limit INT NOT NULL,
    jd_limit INT NOT NULL,
    match_limit INT NOT NULL,

    price_monthly NUMERIC(10,2) NOT NULL,
    price_yearly NUMERIC(10,2) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT uk_plan_name_version UNIQUE (name, version)
);


CREATE TABLE subscription (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT fk_subscription_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenant(id),

    CONSTRAINT fk_subscription_plan
        FOREIGN KEY (plan_id) REFERENCES plan(id)
);

CREATE TABLE invoice (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,

    amount NUMERIC(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,

    due_date DATE NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT fk_invoice_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenant(id),

    CONSTRAINT fk_invoice_subscription
        FOREIGN KEY (subscription_id)
        REFERENCES subscription(id)
);


CREATE TABLE payment (
    id BIGSERIAL PRIMARY KEY,

    invoice_id BIGINT NOT NULL,
    tenant_id BIGINT NOT NULL,

    stripe_payment_intent_id VARCHAR(100),
    amount NUMERIC(10,2) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT fk_payment_invoice
        FOREIGN KEY (invoice_id)
        REFERENCES invoice(id),

    CONSTRAINT fk_payment_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenant(id)
);


CREATE TABLE upload (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    file_type VARCHAR(20) NOT NULL,
    file_path TEXT NOT NULL,
    file_size BIGINT NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT fk_upload_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenant(id),

    CONSTRAINT fk_upload_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);


CREATE TABLE parsed_document (
    id BIGSERIAL PRIMARY KEY,

    upload_id BIGINT NOT NULL UNIQUE,
    tenant_id BIGINT NOT NULL,

    file_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,

    parsed_data JSONB,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT fk_parsed_document_upload
        FOREIGN KEY (upload_id)
        REFERENCES upload(id)
);

CREATE TABLE match_job (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,
    jd_upload_id BIGINT NOT NULL,

    status VARCHAR(20) NOT NULL,

    started_at TIMESTAMP,
    completed_at TIMESTAMP,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT fk_match_job_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenant(id),

    CONSTRAINT fk_match_job_jd
        FOREIGN KEY (jd_upload_id)
        REFERENCES upload(id)
);

CREATE TABLE match_result (
    id BIGSERIAL PRIMARY KEY,
    match_job_id BIGINT NOT NULL,
    tenant_id BIGINT NOT NULL,
    resume_upload_id BIGINT NOT NULL,
    score NUMERIC(5,2) NOT NULL,
    breakdown_json JSONB,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT fk_match_result_job
        FOREIGN KEY (match_job_id) REFERENCES match_job(id),

    CONSTRAINT fk_match_result_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenant(id),

    CONSTRAINT fk_match_result_resume
        FOREIGN KEY (resume_upload_id) REFERENCES upload(id)
);

CREATE TABLE usage_counter (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,

    resume_used INT NOT NULL DEFAULT 0,
    jd_used INT NOT NULL DEFAULT 0,
    match_used INT NOT NULL DEFAULT 0,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT fk_usage_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenant(id),

    CONSTRAINT fk_usage_subscription
        FOREIGN KEY (subscription_id) REFERENCES subscription(id),

    CONSTRAINT uq_usage_subscription UNIQUE (subscription_id)
);
