CREATE TABLE match_result (
    id BIGSERIAL PRIMARY KEY,
    match_job_id BIGINT NOT NULL,
    tenant_id BIGINT NOT NULL,
    resume_upload_id BIGINT NOT NULL,
    score NUMERIC(5,2) NOT NULL,
    breakdown_json JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_match_result_job
        FOREIGN KEY (match_job_id) REFERENCES match_job(id),

    CONSTRAINT fk_match_result_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenant(id),

    CONSTRAINT fk_match_result_resume
        FOREIGN KEY (resume_upload_id) REFERENCES upload(id)
);
