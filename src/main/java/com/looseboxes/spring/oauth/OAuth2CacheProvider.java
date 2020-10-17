package com.looseboxes.spring.oauth;

import java.util.Optional;
import java.util.function.Supplier;
import javax.cache.Cache;

/**
 * @author hp
 */
public interface OAuth2CacheProvider extends Supplier<Cache>{
    
    @Override
    public default Cache get() {
        return this.getCache().orElseThrow(() -> new NullPointerException());
    }
    
    Optional<Cache> getCache();
}
