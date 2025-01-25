package com.sinuedu.board.qna.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.qna.model.mapper.QnaMapper;
import com.sinuedu.board.qna.model.vo.PageInfo;
import com.sinuedu.board.qna.model.vo.Qna;
import com.sinuedu.board.qna.model.vo.reply;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaService {

	private final QnaMapper mapper;
	
	public int getListCount(HashMap<String,String> map) {
		return mapper.getListCount(map);
	}

	public ArrayList<Qna> selectBoardList(HashMap<String,String> map, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return mapper.selectBoardList(map, rowBounds);
	}

	public ArrayList<reply> selectReply(int rNo) {
		return mapper.selectReply(rNo);
	}
	
	public int insertBoard(Qna q) {
		return mapper.insertBoard(q);
	}

	
	 public Qna selectBoard(int qNo, String id) { 
		 Qna q = mapper.selectBoard(qNo);
		 if(q != null && id != null && !q.getUserNick().equals(id)) { 
			 int result = mapper.updateCount(qNo);
			 q.setViews(q.getViews());
			 }else {
		 }
		return q;
	 }

	public int insertReply(reply r) {
		return mapper.insertReply(r);
	}

	public ArrayList<Category> selectCategory() {
		return mapper.selectCategory();
	}

	public int updateBoard(Qna q) {
		return mapper.updateBoard(q);
	}

	public int deletePost(int qNo) {
		return mapper.deletePost(qNo);
	}	
	
	public int noticeBoard(Qna q) {
		return mapper.noticeBoard(q);
	}

	public ArrayList<Qna> selectResult(List<Qna> result) {
		return mapper.selectResult(result);
	}

	


}
