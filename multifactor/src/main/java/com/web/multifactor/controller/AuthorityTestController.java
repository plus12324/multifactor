package com.web.multifactor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorityTestController {

    @GetMapping("/facebook")
    public String facebook() {
        return "facebook";
    }

    @GetMapping("/google")
    public String google() {
        return "google";
    }

    @GetMapping("/kakao")
    public String kakao() {
        return "기본인증 성공!";
    }
    
    @GetMapping("/kakao_auth")
    public String kakao_auth() {
        return "관리자 승인 성공!";
    }
}
