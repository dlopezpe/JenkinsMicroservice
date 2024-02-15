CREATE FUNCTION update_column_modified_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.modified_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_build_metadata_update_column_modified_at BEFORE INSERT OR UPDATE ON "build_metadata" FOR EACH ROW EXECUTE FUNCTION update_column_modified_at();