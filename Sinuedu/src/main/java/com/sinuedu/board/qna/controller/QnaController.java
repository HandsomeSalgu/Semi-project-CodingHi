package com.sinuedu.board.qna.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

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
		int listCount = bService.getListCount();
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5);
		ArrayList<Qna> list = bService.selectBoardList(pi);

		m.addAttribute("list", list).addAttribute("pi", pi);
		m.addAttribute("loc", request.getRequestURI());

		/*
		 * for(Qna q : list) { System.out.println(q); }
		 */

		return "views/question/question-list";
	}

	@GetMapping("write")
	public String insertBoard() {
		
		return "views/question/question-write";
	}

	@PostMapping("insert")
	public String insertBoard(@ModelAttribute Qna q, HttpSession session) {
		/*q.setUserNick(session.getId());*/
		q.setWriter(((Member)session.getAttribute("loginUser")).getUserNo());
		q.setCgNo(3);
		
		// System.out.println(q);
		
		int result = bService.insertBoard(q);
		if (result > 0) {
			return "redirect:/qna/list";
		} else {
			throw new QnaException("게시글 작성을 실패하였습니다.");
		}

	}

		// 상세 페이지 글 조회 이동
		@GetMapping("/{qnaNo}/{page}") // 글번호/페이지
		public ModelAndView selectBoard(@PathVariable("qnaNo") int qNo, @PathVariable("page") int page,
	   		  							HttpSession session, ModelAndView mv) {
		  
				  Member loginUser = (Member)session.getAttribute("loginUser");
				  String id = null;
				  if( loginUser != null) { 
					 	id = loginUser.getUserId();
				  	}
				  
				  Qna q = bService.selectBoard(qNo, id);
				  
				  ArrayList<reply> r = bService.selectReply(qNo);
					/*
					 * for(reply rp : r) { System.out.println(rp); }
					 */
				  
				  //ArrayList<Qna> list = bService.selectBoardList(qNo, id);
				  
				  if(q != null) { 
					  	mv.addObject("q", q).addObject("page",page)
					  	.addObject("r", r).setViewName("views/question/question-post"); 
					  	return mv; 
				  }else { 
						throw new QnaException("게시글 상세조회를 실패하였습니다."); 
					}
			  
			  }
		
		@PostMapping("insertReply")
		public String insertReply(@ModelAttribute Qna q, @ModelAttribute reply r, @RequestParam("page") int page, HttpSession session) {
			r.setUserNo(((Member)session.getAttribute("loginUser")).getUserNo());
			r.setQnaNo(q.getQnaNo());
			
			System.out.println(page);
			int result = bService.insertReply(r);
			if(result > 0) {
				return String.format("redirect:/qna/%d/%d", q.getQnaNo(), page);
			}else {
				throw new QnaException("댓글 등록을 실패하셨습니다.");
			}
		 
		}
		
		@GetMapping("search")
		public String searchTitle(@RequestParam("search") String search, Model model) {
			
			return search;
			
		}
}