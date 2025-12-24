-- 1. Make tenant_id nullable
ALTER TABLE users
    ALTER COLUMN tenant_id DROP NOT NULL;

-- 2. Ensure role column length is sufficient
ALTER TABLE users
    ALTER COLUMN role TYPE VARCHAR(50);

-- 3. Ensure password_hash is NOT NULL
ALTER TABLE users
    ALTER COLUMN password_hash SET NOT NULL;
