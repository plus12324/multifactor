package com.web.multifactor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.multifactor.model.User;
import com.web.multifactor.oauth.annotation.SocialUser;
import com.web.multifactor.repository.jpa.UserRepository;

import net.minidev.json.JSONObject;

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

//	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@PostMapping("/userDel")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam Long idx) {
		Map<String, Object> map = new HashMap<>();
		map.put("userList", userRepository.findAll());
//		j.putAll(map);
		return map;
	}
}
