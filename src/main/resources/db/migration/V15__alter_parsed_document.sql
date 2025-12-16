ALTER TABLE parsed_document
    ALTER COLUMN status SET NOT NULL,
    DROP COLUMN parsed,
    ADD CONSTRAINT uk_parsed_document_upload_id UNIQUE (upload_id),
    ADD CONSTRAINT fk_parsed_upload
        FOREIGN KEY (upload_id) REFERENCES upload(id);
