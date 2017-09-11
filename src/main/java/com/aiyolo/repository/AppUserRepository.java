package com.aiyolo.repository;

import com.aiyolo.entity.AppUser;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    @Cacheable(value="appUsers", unless="#result == null")
    AppUser findFirstByUserIdOrderByIdDesc(String userId);

    @Cacheable(value="appUsers", unless="#result == null")
    AppUser findFirstByPhoneOrderByIdDesc(String phone);

    @Override
    @SuppressWarnings("unchecked")
    @Caching(put = { @CachePut(value="appUsers", key="#p0.getUserId()"), @CachePut(value="appUsers", key="#p0.getPhone()") })
    AppUser save(AppUser appUser);

}
