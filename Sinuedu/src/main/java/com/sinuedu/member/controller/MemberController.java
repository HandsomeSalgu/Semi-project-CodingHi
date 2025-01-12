package com.sinuedu.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/member/")
public class MemberController {
	@GetMapping("join")
	public String join1() {
		
		return "join1";
	}
	
	@GetMapping("join2")
	public String join2() {
		
		return "join2";
	}
	
	@PostMapping("join3")
	public String join3() {
		
		return "join3";
	}
	
	@GetMapping("findId")
	public String findId() {
		
		return "findId";
	}
	
	@GetMapping("findPwd")
	public String findPwd() {
		
		return "findPwd";
	}
	
	
	
	
	
	
}
