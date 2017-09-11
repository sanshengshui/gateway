package com.aiyolo.repository;

import com.aiyolo.entity.UserLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserLogRepository extends PagingAndSortingRepository<UserLog, Long> {

    Page<UserLog> findPageByUsername(Pageable pageable, String username);

}
