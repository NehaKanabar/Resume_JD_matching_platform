CREATE TABLE upload (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    file_type VARCHAR(20) NOT NULL,   -- RESUME / JD
    file_path TEXT NOT NULL,           -- S3 / local path
    file_size BIGINT NOT NULL,          -- BYTES

    status VARCHAR(30) NOT NULL,        -- uploaded / parsing / parsed / failed
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_upload_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenant(id),

    CONSTRAINT fk_upload_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);
