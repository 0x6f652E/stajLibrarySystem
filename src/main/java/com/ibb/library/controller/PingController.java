package com.ibb.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/health")
    public String health() { return "OK"; }

    @GetMapping("/secure/ping")
    public String securePing() { return "PONG"; }
}


