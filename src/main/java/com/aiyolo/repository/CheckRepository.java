package com.aiyolo.repository;

import com.aiyolo.entity.Check;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CheckRepository extends CrudRepository<Check, Long> {


    List<Check> findByImeiOrderByIdDesc(String imei);
}
