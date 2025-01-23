package com.sinuedu.user.manager.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
		Set<String> categories = list.stream().map(Chapter::getCgName).collect(Collectors.toSet());
		model.addAttribute("list", list);
		model.addAttribute("categories", categories);
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

	@PostMapping("/deleteChapter")
	@ResponseBody
	public int deleteChapter(@RequestParam("chapTitle") String chapTitle) {
		return mService.deleteChapter(chapTitle);
	}

}
