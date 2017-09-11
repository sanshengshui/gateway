package com.aiyolo.repository;

import com.aiyolo.entity.AppUserSession;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;

public interface AppUserSessionRepository extends CrudRepository<AppUserSession, Long> {

    @Cacheable(value="appUserSessions", unless="#result == null")
    AppUserSession findFirstByUserIdOrderByIdDesc(String userId);

    @Cacheable(value="appUserSessions", unless="#result == null")
    AppUserSession findFirstBySessionOrderByIdDesc(String session);

    @Override
    @SuppressWarnings("unchecked")
    @Caching(put = { @CachePut(value="appUserSessions", key="#p0.getUserId()"), @CachePut(value="appUserSessions", key="#p0.getSession()") })
    AppUserSession save(AppUserSession appUserSession);

}
