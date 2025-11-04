package com.books.services.embeddings;

import java.util.List;

public interface VectorStoreService<T> {
    void addToVectorStore(List<T> entity);
    void updateInVectorStore(T entity);
    void deleteFromVectorStore(Long id);

}
