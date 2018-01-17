package com.aiyolo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/box")
public class BoxController {

    @RequestMapping("/bind")
    public String bindBox(){
        return "";
    }
}
