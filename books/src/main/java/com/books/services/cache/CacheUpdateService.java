package com.books.services.cache;

public interface CacheUpdateService<T> {
    void updateIfPresent(String cacheName, String cacheKey, T updatedObject);
    void evictIfContains(String cacheName, String cacheKey, Long deleteId);
}
