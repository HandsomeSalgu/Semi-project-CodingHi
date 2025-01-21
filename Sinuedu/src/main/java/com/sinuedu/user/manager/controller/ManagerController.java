package com.sinuedu.user.manager.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.user.manager.model.service.ManagerService;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/manager")
public class ManagerController {

	private final ManagerService mService;

	@GetMapping("/userList")
	public String userList(Model model) {
		List<Member> list = mService.userList();
		model.addAttribute("list", list);
		return "userList";
	}

	@GetMapping("/chapterList")
	public String chapterList(Model model) {
	    List<Chapter> list = mService.chapterList();
	    model.addAttribute("list", list);
	    return "chapterList";
	}

	@GetMapping("/lectureAdd")
	public String lectureAddPage() {
	    return "lectureAdd";
	}

	@GetMapping("/chapterAdd")
	public String chapterAddPage() {
	    return "chapterAdd";
	}

}
