package com.sinuedu.board.lecture.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.sinuedu.board.lecture.model.service.LectureService;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Lecture;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/lecture")
public class LectureController {
	
	private final LectureService cService;
	
	@GetMapping("list")
	public ModelAndView selectLectureList(ModelAndView mv) {
		
		ArrayList<Lecture> list = cService.selectLectureList();
		
		for(Lecture lec : list) {
			int lecNo = lec.getLecNo();
			int capCount = cService.captherCount(lecNo);
			lec.setTotalChap(capCount);
		}
		
		mv.addObject("list", list).setViewName("category");
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView selectLecture(@PathVariable("id") int lecNo, ModelAndView mv) {
		
		Chapter chp = cService.selectLecture(lecNo);
		
		mv.addObject("chapter", chp).setViewName("postlist");
		return mv;
	}

	
}
