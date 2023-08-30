package com.vcloudapi.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/test")
public class TestController {

    @RequestMapping(value="/test2")
    public void test() {
        System.out.println("hi");
    }
}
