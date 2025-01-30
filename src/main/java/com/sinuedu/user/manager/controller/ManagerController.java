package com.sinuedu.user.manager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Lecture;
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
	public String lectureAddPage(Model model) {
		List<Category> categories = mService.categoryList();
		model.addAttribute("categories", categories);
		return "lectureAdd";
	}

	@GetMapping("/chapterAdd")
	public String chapterAdd(Model model) {
		List<Chapter> chapters = mService.chapterList();
		List<Lecture> lectures = mService.lectureList();
		Set<String> categories = chapters.stream().map(Chapter::getCgName).collect(Collectors.toSet());
		model.addAttribute("categories", categories);
		model.addAttribute("lectures", lectures);
		return "chapterAdd";
	}

	@PostMapping("/deleteChapter")
	@ResponseBody
	public int deleteChapter(@RequestParam("chapTitle") String chapTitle) {
		return mService.deleteChapter(chapTitle);
	}

	@PostMapping("/insertChapter")
	@ResponseBody
	public Map<String, Object> insertChapter(@ModelAttribute Chapter chapter) {
		Map<String, Object> response = new HashMap<>();
		try {
			int result = mService.insertChapter(chapter);
			if (result > 0) {
				response.put("success", true);
			} else {
				response.put("success", false);
				response.put("message", "챕터 추가에 실패했습니다.");
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "서버 오류: " + e.getMessage());
		}
		return response;
	}

	@GetMapping("/chapterEdit")
	public String chapterEdit(@RequestParam("chapTitle") String chapTitle, Model model) {
		Chapter chapter = mService.getChapterByTitle(chapTitle);
		List<Chapter> chapters = mService.chapterList();
		List<Lecture> lectures = mService.lectureList();
		Set<String> categories = chapters.stream().map(Chapter::getCgName).collect(Collectors.toSet());

		model.addAttribute("chapter", chapter);
		model.addAttribute("categories", categories);
		model.addAttribute("lectures", lectures);
		return "chapterEdit";
	}

	@PostMapping("/updateChapter")
	@ResponseBody
	public Map<String, Object> updateChapter(@ModelAttribute Chapter chapter) {
		Map<String, Object> response = new HashMap<>();
		try {
			int result = mService.updateChapter(chapter);
			response.put("success", result > 0);
			response.put("message", result > 0 ? "성공적으로 수정되었습니다." : "수정에 실패했습니다.");
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "서버 오류: " + e.getMessage());
		}
		return response;
	}
	
	@PostMapping("/toggleAdmin")
	@ResponseBody
	public Map<String, Object> toggleAdmin(@RequestParam("userId") String userId, @RequestParam("adminStatus") String adminStatus) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        int result = mService.updateAdminStatus(userId, adminStatus);
	        response.put("success", result > 0);
	    } catch (Exception e) {
	        response.put("success", false);
	    }
	    return response;
	}

	@PostMapping("/toggleStatus")
	@ResponseBody
	public Map<String, Object> toggleStatus(@RequestParam("userId") String userId, @RequestParam("status") String status) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        int result = mService.updateUserStatus(userId, status);
	        response.put("success", result > 0);
	    } catch (Exception e) {
	        response.put("success", false);
	    }
	    return response;
	}

}