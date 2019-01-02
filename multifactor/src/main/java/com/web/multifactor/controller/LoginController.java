package com.web.multifactor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by KimYJ on 2017-09-13.
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
    	System.out.println("■ 로그인페이지 이동");
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String loginComplete() {
        return "redirect:/board/list";
    }
}

