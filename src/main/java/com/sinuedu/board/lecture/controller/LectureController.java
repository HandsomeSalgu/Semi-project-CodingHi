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
import com.sinuedu.board.lecture.model.vo.Image;
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
	public ModelAndView selectLectureList(ModelAndView mv, HttpSession session) {
		
		HashMap<String, Integer> map = new HashMap<>();
		
		int userNo = 0;
		if(session.getAttribute("loginUser") != null){
			userNo = ((Member)session.getAttribute("loginUser")).getUserNo();
		}
		
		ArrayList<Lecture> list = cService.selectLectureList(null);
		ArrayList<Image> iList = cService.selectImageList(null);
		
		for(Lecture lec : list) {
			int lecNo = lec.getLecNo();
			int capCount = cService.chapterCount(lecNo);
			lec.setTotalChap(capCount);
			
			ArrayList<Chapter> cList = cService.selectLecture(lecNo);
			
			lec.setSvgRate(svgRate(cList)); 
			
			//유저별 강의 진도율 표시
			lec.setProgressRate(progressRate(capCount, userNo, lecNo));
			
			map.put("userNo", userNo);
			map.put("lecNo", lecNo);
			
			int bookmarkCheck = cService.countBookmark(map);
			if(bookmarkCheck == 1) {
				lec.setBookmarkCheck("Y");
			}else {
				lec.setBookmarkCheck("N");
			}
		}
		
		mv.addObject("list", list).addObject("iList", iList).setViewName("category");
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView selectLecture(@PathVariable("id") int lecNo, ModelAndView mv,
									  HttpSession session) {
		System.out.println(lecNo);
		HashMap<String, Integer> map = new HashMap<>();
		ArrayList<Chapter> cList = cService.selectLecture(lecNo);
		int capCount = cService.chapterCount(lecNo);
		ArrayList<Lecture> lList = cService.selectLectureList(lecNo);
		Lecture lec = lList.get(0);
		Image img = new Image();
		
		ArrayList<Image> iList = cService.selectImageList(lecNo);
		if(!iList.isEmpty()) {
			img = iList.get(0);
		}else {
			img =null;
		}
		
		int userNo = 0;
		if(session.getAttribute("loginUser") != null){
			userNo = ((Member)session.getAttribute("loginUser")).getUserNo();
		}
		
		for(int i =1 ; i<=cList.size() ; i++) {
			cList.get(i-1).setLecChapNum(i);
		}
		
		double svgRate = svgRate(cList);
		int progressRate = progressRate(capCount, userNo, lecNo);
		
		map.put("userNo", userNo);
		map.put("lecNo", lecNo);
		
		int bookmarkCheck = cService.countBookmark(map);
		if(bookmarkCheck == 1) {
			lec.setBookmarkCheck("Y");
		}else {
			lec.setBookmarkCheck("N");
		}
		
		mv.addObject("lecNo",lecNo).addObject("svgRate", svgRate).addObject("progressRate", progressRate);
		mv.addObject("lec", lec).addObject("img", img).addObject("cList", cList).addObject("capCount", capCount).setViewName("postlist");
		return mv;
	}
	
	//Lecture 평균 구하는 메소드
	public double svgRate(ArrayList<Chapter> list) {
		
		int sumRate = 0;
		double svgRate = 0;
		
		ArrayList<Chapter> rateNotNull = new ArrayList<>();
		for(Chapter c : list) {
			sumRate += c.getChapRate();
			
			//평점을 준사람은 무조건 1점부터
			if(c.getChapRate() != 0) {
				rateNotNull.add(c);
			}
		}
	
		
		svgRate = (double)sumRate/rateNotNull.size();
		svgRate = Double.parseDouble(String.format("%.1f", svgRate));
		if(Double.isNaN(svgRate)) {
			svgRate = 0;
		}
		return svgRate;
	}
	
	//유저별 강의 진도율 표시
	public int progressRate(int capCount, int userNo, int lecNo) {
		int result = 0;
		
		if(capCount > 0 && userNo >0) {
			HashMap<String, Integer> map = new HashMap<>();
			map.put("userNo", userNo);
			map.put("lecNo", lecNo);
			
			int userProgressRate = cService.userProgressRate(map);
			result = (int) ((userProgressRate / (double) capCount) * 100);
		}
		return result;
	}
	
	@PostMapping("/{lNo}/{cNo}")
	public ModelAndView selectChapter(@PathVariable("lNo") int lecNo, @PathVariable("cNo") int lecChapNum, 
									  @RequestParam("chapNo") int chapNo,ModelAndView mv,
									  HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		int userNo = 0;
		if(loginUser != null) {
			userNo = loginUser.getUserNo();
			
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
		
		
		Chapter chapter = cService.selectChapter(chapNo, userNo);
		chapter.setLecChapNum(lecChapNum);
		
		mv.addObject("chapNo", chapNo);
		mv.addObject("chapter", chapter).addObject("lecChapNum",lecChapNum).addObject("lecNo",lecNo).setViewName("post");
		
		return mv;
	}
	
	@GetMapping("rating")
	@ResponseBody
	public int rating(@RequestParam("rating") int rating, @RequestParam("chapNo") int chapNo,
					   HttpSession session) {
		System.out.println(1);
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
	
	@GetMapping("/category/{spanVal}")
	public ModelAndView selectCategory(@PathVariable("spanVal") String cgName,
										ModelAndView mv, HttpSession session) {
		System.out.println(cgName);
		//
		
		
		//리스트 만들어 놓기
		ArrayList<Lecture> list = new ArrayList<Lecture>();
		ArrayList<Image> iList = new ArrayList<Image>();
		
		//로그인
		int userNo = 0;
		if(session.getAttribute("loginUser") != null){
			userNo = ((Member)session.getAttribute("loginUser")).getUserNo();
		}
		
		//북마크이면 BOOKMARK, 아니면 CATEGORY 글 모집
		HashMap<String, Object> mapImage = new HashMap<>();
		
		if(cgName.equals("BOOKMARK")) {
			list = cService.bookmarkCategory(userNo);
			
			mapImage.put("userNo", userNo);
		}else {
			list = cService.selectCategory(cgName);
			
			mapImage.put("cgName", cgName);
		}
		
		//이미지 리스트 불러오기
		iList = cService.CBImageList(mapImage);
		
		//아무것도 안담기면 category.html의 th:block if문 자체가 발생이 안돼서 임의의 값 한 개를 넣어줌
		if(iList.isEmpty()) {
			iList.add(0, new Image());
		}
		
		//유저별 강의 진도율 및 북마크 표시
		HashMap<String, Integer> map = new HashMap<>();
		
		for(Lecture lec : list) {
			int lecNo = lec.getLecNo();
			map.put("userNo", userNo);
			map.put("lecNo", lecNo);
			
			int capCount = cService.chapterCount(lecNo);
			lec.setTotalChap(capCount);
			
			ArrayList<Chapter> cList = cService.selectLecture(lecNo);
			
			lec.setSvgRate(svgRate(cList)); 
			
			//유저별 강의 진도율 표시
			lec.setProgressRate(progressRate(capCount, userNo, lecNo));

			int bookmarkCheck = cService.countBookmark(map);
			if(bookmarkCheck == 1) {
				lec.setBookmarkCheck("Y");
			}else {
				lec.setBookmarkCheck("N");
			}	
		}
		
		mv.addObject("list", list).addObject("iList", iList).setViewName("category");
		
//		System.out.println(mv2);
		return mv;
	}
	
	@GetMapping("insertBookmark")
	@ResponseBody
	public int insertBookmark(@RequestParam("lecNo") int lecNo, HttpSession session) {
		
		int userNo = ((Member)session.getAttribute("loginUser")).getUserNo();
		HashMap<String, Integer> map = new HashMap<>();
		map.put("lecNo", lecNo);
		map.put("userNo", userNo);
		int result = 0;
		int bookmarkCheck = cService.countBookmark(map);
		if(bookmarkCheck == 0) {
			result = cService.insertBookmark(map);
		}
		
		return result;
	}
	
	@GetMapping("deleteBookmark")
	@ResponseBody
	public int deleteBookmark(@RequestParam("lecNo") int lecNo, HttpSession session) {
		
		int userNo = ((Member)session.getAttribute("loginUser")).getUserNo();
		HashMap<String, Integer> map = new HashMap<>();
		map.put("lecNo", lecNo);
		map.put("userNo", userNo);
		int result = 0;
		int bookmarkCheck = cService.countBookmark(map);
		if(bookmarkCheck == 1) {
			result = cService.deleteBookmark(map);
		}
		
		return result;
	}
	
	
}
