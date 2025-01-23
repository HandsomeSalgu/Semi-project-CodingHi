package com.sinuedu.board.qna.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.qna.exception.QnaException;
import com.sinuedu.board.qna.model.service.QnaService;
import com.sinuedu.board.qna.model.vo.Image;
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

	@GetMapping("notice")
	public int noticeBoard(@ModelAttribute Qna q) {
		return bService.noticeBoard(q);
	}
	
	/*
	 * @PostMapping("/search") public String handleSearch(@ModelAttribute Qna q,
	 * Model model) { // 선택된 조건값 유지 model.addAttribute("conditions", new
	 * String[]{"-", "writer", "title", "content"}); model.addAttribute("condition",
	 * condition);
	 * 
	 * // 검색 결과 처리 로직 (여기서는 단순히 입력값을 출력) model.addAttribute("search", search);
	 * 
	 * // 예시로 검색 결과를 출력 (실제 로직에서는 DB 조회 등 처리) model.addAttribute("result", "검색 조건: "
	 * + condition + ", 검색어: " + search);
	 * 
	 * return "search"; // 템플릿 파일 이름 (search.html) } }
	 */
	
	
	//summernote에서 받아온 파일 저장하고 반환 메소드
//	@PostMapping(value = "/uploadSummernoteImageFile", produces = "application/json; charset=UTF-8")
//	public void uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {
//		
//		if(!multipartFile.getOriginalFilename().equals("")) {
//			String[] returnArr = saveFile(multipartFile);
//			if(returnArr[1] != null) {
//				Image a = new Image();
//				a.setImgName(multipartFile.getOriginalFilename());
//				a.setImgRename(returnArr[1]);
//				a.setImgPath(returnArr[0]);
//			}
//		}
//		
//	}
//	
//	//첨부파일 폴더에 파일 저장하는 메소드
//	private String[] saveFile(MultipartFile multipartFile) {
//		String savePath = "d:\\dev\\summernote_image";
//		File folder = new File(savePath);
//		if(!folder.exists()) {
//			folder.mkdirs();
//		}
//		//폴더 명이 없으면 폴더 명 생성
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		//날짜를 원하는 대로 가져오고 싶을 때 사용
//		
//		int ranNum = (int)(Math.random()*100000);
//		String originFileName = multipartFile.getOriginalFilename();
//		String renameFileName = sdf.format(new Date()) + ranNum + originFileName.substring(originFileName.lastIndexOf("."));
//		
//		String renamePath = folder + "\\" + renameFileName;
//		
//		try {
//			multipartFile.transferTo(new File(renamePath));
//		} catch (IllegalStateException | IOException e) {
//			System.out.println("파일 전송 에러 : " + e.getMessage());
//		}
//		
//		String[] returnArr = new String[2];
//		returnArr[0] = savePath;
//		returnArr[1] = renameFileName;
//		
//		return returnArr;
//	}
}