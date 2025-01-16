package com.sinuedu.board.qna.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.sinuedu.board.qna.model.mapper.QnaMapper;
import com.sinuedu.board.qna.model.vo.PageInfo;
import com.sinuedu.board.qna.model.vo.Qna;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaService {

	private final QnaMapper mapper;
	
	public int getListCount() {
		return mapper.getListCount();
	}

	public ArrayList<Qna> selectBoardList(PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return mapper.selectBoardList(rowBounds);
	}

	public int insertBoard(Qna q) {
		return mapper.insertBoard(q);
	}

	
//	 public Qna selectBoard(int qNo, String id) { 
//		 Qna q = mapper.selectBoard(qNo);
//		 if(q != null && id != null && !q.getUserNick().equals(id)) { 
//			 int result = mapper.updateViews(qNo); q.setViews(q.getViews()); 
//			 } 
//		 return null; 
//		 }

	public Qna selectBoard(int qNo) {
		return mapper.selectBoard(qNo);
	}


//	public Qna selectBoard(int qNo, int page) {
//		return mapper.selectBoard(qNo,page);
//	}
		
}
