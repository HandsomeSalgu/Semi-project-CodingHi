package com.sinuedu.user.member.controller;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.sinuedu.user.member.exception.MemberException;
import com.sinuedu.user.member.model.service.MemberService;
import com.sinuedu.user.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/member/")
public class MemberController {
	
	private final MemberService mService;
	
	private final BCryptPasswordEncoder bcrypt;    // 복호화 불가능하게 만드는 역할
	
//	@GetMapping("login")
//	public String login(HttpSession session, Model model) {
//	    // 세션에 로그인 정보가 있는 경우 홈 화면으로 리다이렉트
//	    if (session.getAttribute("loginUser") != null) {
//	        return "redirect:/";
//	    }
//
//	    // 로그인 페이지로 이동하면서 현재 로그인 상태를 모델에 추가
//	    model.addAttribute("isLoggedIn", false);
//	    return "index"; // 로그인 페이지로 이동
//	}

//	@GetMapping("login")
//	public String login(@ModelAttribute Member m, Model model) {
//		Member loginUser = mService.login(m);
//		if( loginUser != null) {
//			model.addAttribute("loginUser", loginUser);
//			return "redirect:/";
//		}else {
//			throw new MemberException("입력하신 정보가 옳지 않습니다");
//		}
//	}
	
	@PostMapping("login")
	public String login(Member m, HttpSession session) {
		Member loginUser = mService.login(m);
		System.out.println(m);
		System.out.println(bcrypt.encode("1234"));
		System.out.println(loginUser);
		if(loginUser != null && bcrypt.matches(m.getUserPw(), loginUser.getUserPw())) {
			session.setAttribute("loginUser", loginUser);
			return "redirect:/";
		} else {
			throw new MemberException("로그인을 실패하였습니다");
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
	
	@PostMapping("join2")
	public String join2(@ModelAttribute Member m,Model model) {
		System.out.println("userId : " + m.getUserId());
		model.addAttribute("member", m);
		return "join2";
	}
	
	@PostMapping("join3")
	public String join3(@ModelAttribute Member m, 
						@RequestParam("phone1") String phone1, 
						@RequestParam("phone2") String phone2, 
						@RequestParam("phone3") String phone3, 
						@RequestParam("birth1") String birth1,
						@RequestParam("birth2") String birth2, 
						@RequestParam("birth3") String birth3) throws ParseException {
		m.setPhone(phone1 + "-" + phone2 + "-" + phone3);
		
		String Birth = birth1+birth2+birth3;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = formatter.parse(Birth);
        m.setBirthDate(date);
        m.setUserPw(bcrypt.encode(m.getUserPw()));
        System.out.println(m);
		int result = mService.insertMember(m);
		
		
		
		if(result > 0) {
			return "join3";
		} else {
			throw new MemberException("회원가입을 실패하였습니다");
		}
	}
	
	@GetMapping("checkId")
	public void checkId(@RequestParam("id") String id, PrintWriter out) {
		int count = mService.checkId(id);
		out.print(count);
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
