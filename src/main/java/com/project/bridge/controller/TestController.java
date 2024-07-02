package com.project.bridge.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

        @RequestMapping("/hello")
        public String hello() {
            return "Hello, World!";
        }
}
