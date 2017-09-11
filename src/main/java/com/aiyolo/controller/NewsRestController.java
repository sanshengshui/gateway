package com.aiyolo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiyolo.entity.News;
import com.aiyolo.repository.NewsRepository;

@RestController
@RequestMapping("/news")
public class NewsRestController {

    @Autowired NewsRepository newsRepository;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list(
            @RequestParam(value = "draw", defaultValue = "0") Integer draw,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "20") Integer length) {
        length = length > 0 ? length : 20;

        Order order1 = new Order(Direction.DESC, "top");
        Order order2 = new Order(Direction.DESC, "createdAt");
        List<Order> sortList = new ArrayList<Order>();
        sortList.add(order1);
        sortList.add(order2);
        Sort sort = new Sort(sortList);
        Pageable pageable = new PageRequest((start / length), length, sort);
        Page<News> page = newsRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("draw", draw);
        response.put("recordsTotal", page.getTotalElements());
        response.put("recordsFiltered", page.getTotalElements());
        response.put("data", page.getContent());
        return response;
    }

}
