package com.web.multifactor.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
		userRepository.deleteById(idx);
		Map<String, Object> map = new HashMap<>();
		map.put("userList", userRepository.findAll());
		return map;
	}
	
	@PostMapping("/userSave")
	@ResponseBody
	public Map<String, Object> save(User user) {		
		User txUser = userRepository.findOneByIdx(user.getIdx());
		txUser.setIdx(user.getIdx());
		txUser.setSocialType(user.getSocialType());
		txUser.setUpdatedDate(LocalDateTime.now());
		userRepository.save(txUser)	;
		
		Map<String, Object> map = new HashMap<>();
		map.put("userList", userRepository.findAll());
		return map;
	}
}
