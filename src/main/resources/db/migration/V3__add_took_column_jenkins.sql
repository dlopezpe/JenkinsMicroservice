ALTER TABLE build_metadata ADD COLUMN "took" BIGINT;
ALTER TABLE build_metadata ADD COLUMN "estimated_duration" BIGINT;
ALTER TABLE build_metadata ADD COLUMN "timestamp" timestamp;
ALTER TABLE build_metadata ADD COLUMN "log_build" VARCHAR(255);