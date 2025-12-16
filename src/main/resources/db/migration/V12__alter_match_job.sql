-- Remove columns not present in entity
ALTER TABLE match_job
DROP COLUMN IF EXISTS triggered_by_user_id,
DROP COLUMN IF EXISTS created_at;

-- Add columns required by entity
ALTER TABLE match_job
ADD COLUMN IF NOT EXISTS started_at TIMESTAMP,
ADD COLUMN IF NOT EXISTS completed_at TIMESTAMP;
