CREATE TABLE match_job (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    jd_upload_id BIGINT NOT NULL,
    triggered_by_user_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_match_job_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenant(id),

    CONSTRAINT fk_match_job_jd
        FOREIGN KEY (jd_upload_id) REFERENCES upload(id),

    CONSTRAINT fk_match_job_user
        FOREIGN KEY (triggered_by_user_id) REFERENCES users(id)
);
