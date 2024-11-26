-- ADD SEARCH VECTOR ADMI_PRODUCT IF NOT EXISTS

ALTER TABLE admi_product
    ADD COLUMN search_vector tsvector
    GENERATED ALWAYS AS (to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(description, ''))) STORED;

-- INDEX SEARCH VECTOR ADMI_PRODUCT

CREATE INDEX IF NOT EXISTS idx_search_vector ON admi_product USING gin(search_vector);
