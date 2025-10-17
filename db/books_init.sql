CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS vector_store (
    id UUID PRIMARY KEY,
    content TEXT,
    metadata JSONB,
    embedding VECTOR(1408)
);

CREATE INDEX IF NOT EXISTS vector_store_idx
    ON vector_store
    USING hnsw (embedding vector_cosine_ops);
