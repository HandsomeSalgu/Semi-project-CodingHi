package com.sinuedu.user.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.sinuedu.user.member.exception.MemberException;
import com.sinuedu.user.member.model.service.MemberService;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/member/")
public class MemberController {
	
	private final MemberService mService;
	
	@GetMapping("login")
	public String login(@ModelAttribute Member m, Model model) {
		Member loginUser = mService.login(m);
		if( loginUser != null) {
			model.addAttribute("loginUser", loginUser);
			return "redirect:/";
		}else {
			throw new MemberException("입력하신 정보가 옳지 않습니다");
		}
	}
	
	@GetMapping("logout")
	public String login(SessionStatus session) {
		session.setComplete();
		return "redirect:/";
	}
	
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
