package com.aiyolo.repository;

import java.util.List;

import com.aiyolo.entity.News;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NewsRepository extends PagingAndSortingRepository<News, Long> {

    @Query("select n from News n where status=1 order by top desc,createdAt desc")
    List<News> findAllOrderByTopDescAndCreatedAtDesc();

}
