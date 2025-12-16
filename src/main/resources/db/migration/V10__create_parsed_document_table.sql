CREATE TABLE parsed_document (
    id BIGSERIAL PRIMARY KEY,
    upload_id BIGINT NOT NULL,
    tenant_id BIGINT NOT NULL,
    file_type VARCHAR(20) NOT NULL,
    parsed_data JSONB,
    parsed BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
