package com.sinuedu.board.qna.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.qna.exception.QnaException;
import com.sinuedu.board.qna.model.service.QnaService;
import com.sinuedu.board.qna.model.vo.PageInfo;
import com.sinuedu.board.qna.model.vo.Qna;
import com.sinuedu.board.qna.model.vo.reply;
import com.sinuedu.common.Pagination;
import com.sinuedu.user.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/qna")
public class QnaController {

	private final QnaService bService;

	@GetMapping("list")
	public String selectList(@RequestParam(value = "page", defaultValue = "1") int currentPage, Model m,
			HttpServletRequest request) {
		int listCount = bService.getListCount(null);

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5);
		ArrayList<Qna> list = bService.selectBoardList(null, pi);

		m.addAttribute("list", list).addAttribute("pi", pi);
		m.addAttribute("loc", request.getRequestURI());

		/*
		 * for(Qna q : list) { System.out.println(q); }
		 */

		return "views/question/question-list";
	}

	@GetMapping("write")
	public ModelAndView insertBoard(ModelAndView mv) {
		ArrayList<Category> categories = bService.selectCategory();

		if (categories != null) {
			mv.addObject("categories", categories).setViewName("question-write");
			return mv;
		} else {
			throw new QnaException("카테고리 목록이 없습니다.");
		}

	}

	@PostMapping("insert")
	public String insertBoard(@ModelAttribute Qna q, HttpSession session, @RequestParam(value="noticeBox", required = false) boolean noticeBox) {
		/* q.setUserNick(session.getId()); */
		q.setWriter(((Member) session.getAttribute("loginUser")).getUserNo());
		
		String notice = noticeBox ? "Y" : "N";
		//System.out.println(notice);

		q.setNotice(notice);
		System.out.println(q);

		int result = bService.insertBoard(q);
		if (result > 0) {
			return "redirect:/qna/list";
		} else {
			throw new QnaException("게시글 작성을 실패하였습니다.");
		}
	}

	// 상세 페이지 글 조회 이동
	@GetMapping("/{qnaNo}/{page}") // 글번호/페이지
	public ModelAndView selectBoard(@PathVariable("qnaNo") int qNo, @PathVariable("page") int page, HttpSession session,
			ModelAndView mv) {

		Member loginUser = (Member) session.getAttribute("loginUser");
		String id = null;
		if (loginUser != null) {
			id = loginUser.getUserId();
		}

		Qna q = bService.selectBoard(qNo, id);

		ArrayList<reply> r = bService.selectReply(qNo);
		/*
		 * for(reply rp : r) { System.out.println(rp); }
		 */

		// ArrayList<Qna> list = bService.selectBoardList(qNo, id);

		if (q != null) {
			mv.addObject("q", q).addObject("page", page).addObject("r", r).setViewName("views/question/question-post");
			return mv;
		} else {
			throw new QnaException("게시글 상세조회를 실패하였습니다.");
		}

	}

	@PostMapping("insertReply")
	public String insertReply(@ModelAttribute Qna q, @ModelAttribute reply r, @RequestParam("page") int page,
			HttpSession session) {
		r.setUserNo(((Member) session.getAttribute("loginUser")).getUserNo());
		r.setQnaNo(q.getQnaNo());

		System.out.println(page);
		int result = bService.insertReply(r);
		if (result > 0) {
			return String.format("redirect:/qna/%d/%d", q.getQnaNo(), page);
		} else {
			throw new QnaException("댓글 등록을 실패하셨습니다.");
		}

	}

	
	@GetMapping("/{qnaNo}/{page}/updatePost")  
	public ModelAndView updatePost(ModelAndView mv, @PathVariable("page") int page, @PathVariable("qnaNo") int qNo
									, HttpSession session) { 
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String id = null;
		if (loginUser != null) {
			id = loginUser.getUserId();
		}
		
		
		ArrayList<Category> categories = bService.selectCategory();
		
		Qna q = bService.selectBoard(qNo, id);
		
		System.out.println(page);
		if (categories != null) {
			mv.addObject("page", page).addObject("q", q);
			mv.addObject("categories", categories).setViewName("question-update");
			return mv;
		} else {
			throw new QnaException("카테고리 목록이 없습니다.");
		}
	}
	
	@PostMapping("updatePost")
	public String updateBoard(@ModelAttribute Qna q, @ModelAttribute reply r, @RequestParam("page") int page, HttpSession session) {
		q.setWriter(((Member) session.getAttribute("loginUser")).getUserNo());

		System.out.println(q);

		int result = bService.updateBoard(q);
		if (result > 0) {
			return String.format("redirect:/qna/%d/%d", q.getQnaNo(), page);
		} else {
			throw new QnaException("게시글 수정을 실패하였습니다.");
		}
	}

	@PostMapping("deletePost")
    public String deletePost(@RequestParam("qnaNo") int qNo) {
        bService.deletePost(qNo);
        return "redirect:/qna/list";
	}
	
	@GetMapping("notice")
	public int noticeBoard(@ModelAttribute Qna q) {
		return bService.noticeBoard(q);
	}
	
	@GetMapping("search")
	@ResponseBody
	public HashMap<String, Object> search(@RequestParam(value = "page", defaultValue = "1") int currentPage,
								 @RequestParam(value="condition", required = false) String condition, 
								 @RequestParam(value = "search", required = false) String search){
		// 검색 조건 및 검색어 유효성 검사
		System.out.println(condition);
		System.out.println(search);
		
		if(condition == null || condition.equals("-")) {
			throw new QnaException("검색 조건을 선택해 주세요.");
		}
		if(search == null || search.trim().isEmpty()) {
			throw new QnaException("검색어를 입력해주세요.");
		}
		
		// 조건에 따른 리스트 카운트 추가
		HashMap<String, String> map = new HashMap<>();
		map.put("search", search);
		map.put("condition", condition);
		
		int listCount = bService.getListCount(map);
		System.out.println("리스트 카운트 : " + listCount);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5);
		
		// 조건에 따른 검색 처리
		ArrayList<Qna> list = bService.selectBoardList(map, pi);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		result.put("pi", pi);
		
		System.out.println(result);
		
		return result;
	}
}