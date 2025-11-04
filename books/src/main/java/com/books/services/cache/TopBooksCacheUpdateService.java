package com.books.services.cache;

import com.books.dto.BookItemDto;
import com.books.dto.BookItemListDto;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TopBooksCacheUpdateService implements CacheUpdateService<BookItemDto> {

    private final CacheManager cacheManager;

    public TopBooksCacheUpdateService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void updateIfPresent(String cacheName, String cacheKey, BookItemDto updatedObject) {
        BookItemListDto cached = getCachedList(cacheName, cacheKey);
        if (cached == null) return;

        List<BookItemDto> top = cached.getBooks();
        boolean changed = false;
        List<BookItemDto> updatedList = new ArrayList<>(top.size());

        for (BookItemDto item : top) {
            if (Objects.equals(item.getId(), updatedObject.getId())) {
                updatedList.add(updatedObject);
                changed = true;
            } else {
                updatedList.add(item);
            }
        }

        if (changed) {
            cached.setBooks(updatedList);
            putCache(cacheName, cacheKey, cached);
        }
    }

    @Override
    public void evictIfContains(String cacheName, String cacheKey, Long deleteId) {
        BookItemListDto cached = getCachedList(cacheName, cacheKey);
        if (cached == null) return;

        boolean present = cached.getBooks().stream()
                .anyMatch(b -> Objects.equals(b.getId(), deleteId));

        if (present) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) cache.evict(cacheKey);
        }
    }

    private BookItemListDto getCachedList(String cacheName, String cacheKey) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) return null;

        Cache.ValueWrapper wrapper = cache.get(cacheKey);
        if (wrapper == null) return null;

        BookItemListDto cached = (BookItemListDto) wrapper.get();
        if (cached == null || cached.getBooks() == null || cached.getBooks().isEmpty()) return null;

        return cached;
    }

    private void putCache(String cacheName, String cacheKey, BookItemListDto value) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(cacheKey, value);
        }
    }
}