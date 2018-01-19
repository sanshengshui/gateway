package com.aiyolo.repository;

import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.vo.DeviceAlarmSearchVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DeviceAlarmSpecification {

    public static Specification<DeviceAlarm> getSpec(DeviceAlarmSearchVo deviceAlarmSearchVo) {
        return new Specification<DeviceAlarm>() {

            @Override
            public Predicate toPredicate(Root<DeviceAlarm> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (StringUtils.isNotEmpty(deviceAlarmSearchVo.getAreaCode())) {
                    predicates.add(cb.isTrue(cb.function(
                            "regexp",
                            Boolean.class,
                            root.get("gateway").get("areaCode"),
                            cb.literal(deviceAlarmSearchVo.getAreaCode()))));
                }
                if (StringUtils.isNotEmpty(deviceAlarmSearchVo.getVillage())) {
                    predicates.add(cb.like(root.get("gateway").get("village"), "%" + deviceAlarmSearchVo.getVillage() + "%"));
                }
                if (StringUtils.isNotEmpty(deviceAlarmSearchVo.getAddress())) {
                    predicates.add(cb.like(root.get("gateway").get("address"), "%" + deviceAlarmSearchVo.getAddress() + "%"));
                }
                if (deviceAlarmSearchVo.getStatus() >= 0) {
                    predicates.add(cb.equal(root.get("status"), deviceAlarmSearchVo.getStatus()));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }

        };
    }

}
