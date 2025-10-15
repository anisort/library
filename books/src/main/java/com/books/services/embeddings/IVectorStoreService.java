package com.books.services.embeddings;

public interface IVectorStoreService<T> {
    void addToVectorStore(T entity);
    void updateInVectorStore(T entity);
    void deleteFromVectorStore(Long id);

}
