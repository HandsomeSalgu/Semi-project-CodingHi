package com.sinuedu.board.lecture.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.sinuedu.board.lecture.model.exception.LectureException;
import com.sinuedu.board.lecture.model.service.LectureService;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Lecture;
import com.sinuedu.user.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;
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
		Lecture lec = lList.get(0);
		
		for(int i =1 ; i<=cList.size() ; i++) {
			cList.get(i-1).setLecChapNum(i);
		}
		
		int sumRate = 0;
		double svgRate = 0;
		ArrayList<Chapter> rateNotNull = new ArrayList<>();
		for(Chapter c : cList) {
			sumRate += c.getChapRate();
			
			//평점을 준사람은 무조건 1점부터
			if(c.getChapRate() != 0) {
				rateNotNull.add(c);
			}
		}
		
		System.out.println(rateNotNull.size());
		System.out.println(sumRate);
	
		svgRate = (double)sumRate/rateNotNull.size();
		svgRate = Double.parseDouble(String.format("%.1f", svgRate));
		if(Double.isNaN(svgRate)) {
			svgRate = 0;
		}
		
		
		mv.addObject("lecNo",lecNo).addObject("svgRate", svgRate);
		mv.addObject("lec", lec).addObject("cList", cList).addObject("capCount", capCount).setViewName("postlist");
		return mv;
	}
	
	@PostMapping("/{lNo}/{cNo}")
	public ModelAndView selectChapter(@PathVariable("lNo") int lecNo, @PathVariable("cNo") int lecChapNum, 
									  @RequestParam("chapNo") int chapNo,ModelAndView mv,
									  HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		if(loginUser != null) {
			int userNo = loginUser.getUserNo();
			
			HashMap<String, Integer> map = new HashMap<>();
			map.put("chapNo", chapNo);
			map.put("userNo", userNo);
			int result1 = cService.dupViewChapter(map);
			if(result1 == 0) {
				cService.viewChapter(map);
			}
		}else {
			throw new LectureException("로그인이 되어있지 않습니다");
		}
		
		
		Chapter chapter = cService.selectChapter(chapNo);
		chapter.setLecChapNum(lecChapNum);
		
		mv.addObject("chapNo", chapNo);
		mv.addObject("chapter", chapter).addObject("lecChapNum",lecChapNum).addObject("lecNo",lecNo).setViewName("post");
		
		return mv;
	}
	
	@GetMapping("rating")
	@ResponseBody
	public int rating(@RequestParam("rating") int rating, @RequestParam("chapNo") int chapNo,
					   HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		int userNo = loginUser.getUserNo();
		System.out.println(loginUser);
		System.out.println("chapNo : " + chapNo);
		System.out.println("rating : " + rating);
		
		HashMap<String, Integer> map = new HashMap<>();
		map.put("chapNo", chapNo);
		map.put("rating", rating);
		map.put("userNo", userNo);
		
		int result = cService.rating(map);
		if(result >0) {
			cService.chapRateAvg(map);
			return result;
		}else {
			throw new LectureException("별점이 추가되지 않았습니다");
		}
		
	}
	
	
}
