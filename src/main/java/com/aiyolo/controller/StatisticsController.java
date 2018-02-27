package com.aiyolo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/data")
public class StatisticsController {

    @RequestMapping("/statistics")
    public String Statistics(Model model) {
        return "statistics";
    }
}
