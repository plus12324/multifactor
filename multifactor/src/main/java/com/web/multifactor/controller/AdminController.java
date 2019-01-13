package com.web.multifactor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.multifactor.model.User;
import com.web.multifactor.oauth.annotation.SocialUser;
import com.web.multifactor.repository.jpa.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/userList")
	public String list(@PageableDefault Pageable pageable, @SocialUser User user, Model model) {
		model.addAttribute("userList", userRepository.findAll());
		return "/admin/userlist";
	}

	@PostMapping("/userDel")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam Long idx) {
//		userRepository.deleteById(idx);
		Map<String, Object> map = new HashMap<>();
		map.put("userList", userRepository.findAll());
		return map;
	}
	
	
//	w.s.m.s.DefaultHandlerExceptionResolver : 
//	Resolved [org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: 
//	Cannot construct instance of `com.web.multifactor.model.User` 
//	(although at least one Creator exists): no String-argument constructor/factory method to deserialize from String value ('idx=3&socialType=KAKAO'); 
//	nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException: 
//	Cannot construct instance of `com.web.multifactor.model.User` (although at least one Creator exists): 
//	no String-argument constructor/factory method to deserialize from String value ('idx=3&socialType=KAKAO')
//	 at [Source: (PushbackInputStream); line: 1, column: 1]]
	@PostMapping("/userSave")
	@ResponseBody
	public Map<String, Object> save( @RequestBody User user, BindingResult bidBindingResult) {
		System.out.println(user.getIdx());
		
		if(bidBindingResult.hasErrors()) {
			System.out.println("dddddddddd");
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("userList", userRepository.findAll());
		return map;
	}
}
