package com.sinuedu.board.lecture.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
		
		ArrayList<Lecture> list = cService.selectLectureList(null);
		
		for(Lecture lec : list) {
			int lecNo = lec.getLecNo();
			int capCount = cService.chapterCount(lecNo);
			lec.setTotalChap(capCount);
		}
		
		mv.addObject("list", list).setViewName("category");
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView selectLecture(@PathVariable("id") int lecNo, ModelAndView mv) {
		
		ArrayList<Chapter> cList = cService.selectLecture(lecNo);
		int capCount = cService.chapterCount(lecNo);
		ArrayList<Lecture> lList = cService.selectLectureList(lecNo);
		for(Chapter le : cList) {
			System.out.println(le);
		}
		Lecture lec = lList.get(0);
		
		for(int i =1 ; i<=cList.size() ; i++) {
			cList.get(i-1).setLecChapNum(i);
		}
		mv.addObject("lecNo",lecNo);
		mv.addObject("lec", lec).addObject("cList", cList).addObject("capCount", capCount).setViewName("postlist");
		return mv;
	}
	
	@PostMapping("/{lNo}/{cNo}")
	public ModelAndView selectChapter(@PathVariable("lNo") int lecNo, @PathVariable("cNo") int lecChapNum, 
									  @RequestParam("chapNo") int chapNo,ModelAndView mv) {
		System.out.println(chapNo);
		Chapter chapter = cService.selectChapter(chapNo);
		chapter.setLecChapNum(lecChapNum);
		System.out.println(chapter);
		mv.addObject("chapNo", chapNo);
		mv.addObject("chapter", chapter).addObject("lecChapNum",lecChapNum).addObject("lecNo",lecNo).setViewName("post");
		
		return mv;
	}
	
	@GetMapping("rating")
	@ResponseBody
	public void rating(@RequestParam("rating") int rating, @RequestParam("lecNo") int lecNo) {
		
	}
	
	
}
