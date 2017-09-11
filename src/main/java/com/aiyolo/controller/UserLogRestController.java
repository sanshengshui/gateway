package com.aiyolo.controller;

import com.aiyolo.entity.UserLog;
import com.aiyolo.repository.UserLogRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class UserLogRestController {

    @Autowired
    UserLogRepository userLogRepository;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list(
            @RequestParam(value = "draw", defaultValue = "0") Integer draw,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "20") Integer length,
            @RequestParam(value = "username", defaultValue = "") String username) {
        length = length > 0 ? length : 20;
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest((start / length), length, sort);

        Page<UserLog> page = new PageImpl<UserLog>(new ArrayList<UserLog>());
        if (StringUtils.isNotEmpty(username)) {
            page = userLogRepository.findPageByUsername(pageable, username);
        } else {
            page = userLogRepository.findAll(pageable);
        }

        List<UserLog> records = new ArrayList<UserLog>();
        for (int i = 0; i < page.getContent().size(); i++) {
            UserLog record = page.getContent().get(i);
            record.setArgs("");
            records.add(record);
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("draw", draw);
        response.put("recordsTotal", page.getTotalElements());
        response.put("recordsFiltered", page.getTotalElements());
        response.put("data", page.getContent());
        return response;
    }

}
