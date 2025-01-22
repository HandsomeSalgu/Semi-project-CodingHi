package com.sinuedu.user.member.controller;

import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
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
import org.springframework.web.client.RestTemplate;

import com.sinuedu.user.member.exception.MemberException;
import com.sinuedu.user.member.model.service.MemberService;
import com.sinuedu.user.member.model.vo.Member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
		System.out.println(bcrypt.encode("admin"));
		Member loginUser = mService.login(m);
		if(loginUser != null && bcrypt.matches(m.getUserPw(), loginUser.getUserPw())) {
			session.setAttribute("loginUser", loginUser);
			System.out.println(loginUser);
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
	public String join3(
	        @ModelAttribute Member m,
	        @RequestParam("phone1") String phone1,
	        @RequestParam("phone2") String phone2,
	        @RequestParam("phone3") String phone3,
	        @RequestParam("birth1") String birth1,
	        @RequestParam("birth2") String birth2,
	        @RequestParam("birth3") String birth3,
	        @RequestParam("g-recaptcha-response") String recaptchaResponse,
	        HttpServletResponse response) throws ParseException {

	    // 1. reCAPTCHA 검증
	    String secretKey = "6LdsX78qAAAAAAJawRV6tzp60CI-vGiy4oY6-iC3"; // Google 비밀 키
	    String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

	    RestTemplate restTemplate = new RestTemplate();
	    Map<String, String> body = new HashMap<>();
	    body.put("secret", secretKey);
	    body.put("response", recaptchaResponse);

	    // Google API에 검증 요청
	    ResponseEntity<Map> apiResponse = restTemplate.postForEntity(verifyUrl, body, Map.class);

	    // 응답 확인
	    if (apiResponse.getBody() == null || !Boolean.TRUE.equals(apiResponse.getBody().get("success"))) {
	        throw new MemberException("reCAPTCHA 인증에 실패했습니다.");
	    }

	    // 2. 쿠키 추가 (reCAPTCHA 응답 저장)
	    Cookie recaptchaCookie = new Cookie("g-recaptcha-response", recaptchaResponse);
	    recaptchaCookie.setHttpOnly(true); // JavaScript에서 접근 불가
	    recaptchaCookie.setSecure(true);  // HTTPS 요청에서만 전송
	    recaptchaCookie.setPath("/");     // 사이트 전역에서 사용 가능
	    recaptchaCookie.setMaxAge(60 * 5); // 쿠키 유효 시간 (5분)
	    response.addCookie(recaptchaCookie);

	    // 3. 사용자 정보 처리
	    m.setPhone(phone1 + "-" + phone2 + "-" + phone3);

	    String Birth = birth1 + "-" + birth2 + "-" + birth3;
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    java.util.Date utilDate = formatter.parse(Birth);

	    Date date = new Date(utilDate.getTime());
	    m.setBirthDate(date);
	    m.setUserPw(bcrypt.encode(m.getUserPw()));

	    // 4. 회원 가입 로직
	    int result = mService.insertMember(m);

	    // 5. 처리 결과 반환
	    if (result > 0) {
	        return "join3"; // 성공 페이지로 이동
	    } else {
	        throw new MemberException("회원가입을 실패하였습니다");
	    }
	}
	
	@GetMapping("checkId")
	public void checkId(@RequestParam("userId") String userId, PrintWriter out) {
		int count = mService.checkId(userId);
		out.print(count);
	}
	
	@GetMapping("checkUserNick")
	public void checkUserNick(@RequestParam("userNick") String userNick, PrintWriter out) {
		int count = mService.checkUserNick(userNick);
		out.print(count);
	}
	
	// 아이디 찾기
	@GetMapping("find-id")
	public String findId() {
		return "find-id";
	}
	
	@PostMapping("find-id")
	public String findMyId(@ModelAttribute Member m,
						   @RequestParam("userName") String userName,
						   @RequestParam("phone1") String phone1, 
						   @RequestParam("phone2") String phone2, 
						   @RequestParam("phone3") String phone3, 
						   @RequestParam("birth1") String birth1,
						   @RequestParam("birth2") String birth2, 
						   @RequestParam("birth3") String birth3,
						   Model model) throws ParseException {
			
		
		m.setUserName(userName);
		m.setPhone(phone1 + "-" + phone2 + "-" + phone3);
	    
		String Birth = birth1 + "-" + birth2+ "-" +birth3;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = formatter.parse(Birth);
        
        Date date = new Date(utilDate.getTime());
        m.setBirthDate(date);
        
	    String userId = mService.findMyId(m);
			
		if(userId != null) {
			model.addAttribute("userId", userId);
			return "find-id-success";
		} else {
			return "find-id-error";
		}
	}
	
	// 비밀번호 찾기
	@GetMapping("find-pwd")
	public String findPwd() {
		
		return "find-pwd";
	}
	
	// 마이페이지
	@GetMapping("my-page")
	public String edit() {
		return "my-page";
	}
	
	// 내 정보 수정
	@PostMapping("my-page")
	public String editMyInfo(@ModelAttribute Member m,
							 @RequestParam("newUserPw") String newPw, 
						   	 @RequestParam("phone1") String phone1, 
							 @RequestParam("phone2") String phone2, 
							 @RequestParam("phone3") String phone3, 
							 @RequestParam("birth1") String birth1,
							 @RequestParam("birth2") String birth2, 
							 @RequestParam("birth3") String birth3, HttpSession session) throws ParseException {
		Member loginUser =(Member)session.getAttribute("loginUser");
		
		String Phone = phone1 + "-" + phone2 + "-" + phone3;
		m.setPhone(Phone);
		
		String Birth = birth1 + "-" + birth2 + "-" + birth3;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = formatter.parse(Birth);
        
        Date date = new Date(utilDate.getTime());
        
        m.setBirthDate(date);
        m.setUserId(loginUser.getUserId());
        
        
        if (bcrypt.matches(newPw, m.getUserPw())) {
            throw new MemberException("새 비밀번호는 기존 비밀번호와 다르게 설정해주세요.");
        }
        
        m.setUserPw(bcrypt.encode(newPw));

        // 비밀번호 암호화 및 업데이트
        HashMap<String, String> map = new HashMap<>();
        map.put("id", m.getUserId());
        map.put("newUserPw", bcrypt.encode(newPw));

		int result = mService.updateMember(m);
		System.out.println("result : " + result);
		
		if(result > 0) {
			Member updatedUser = mService.login(m);
	        session.setAttribute("loginUser", updatedUser);
	        System.out.println(updatedUser);
			return "redirect:/";
		} else {
			throw new MemberException("회원정보 수정을 실패하였습니다");
		}
	}
	
	
}
