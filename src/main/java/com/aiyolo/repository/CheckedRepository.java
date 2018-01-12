package com.aiyolo.repository;

import com.aiyolo.entity.Checked;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CheckedRepository extends CrudRepository<Checked, Long> {


    List<Checked> findByImeiOrderByIdDesc(String imei);
}
